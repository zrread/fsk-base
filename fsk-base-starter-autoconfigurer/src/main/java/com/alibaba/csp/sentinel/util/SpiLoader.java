package com.alibaba.csp.sentinel.util;

import com.alibaba.csp.sentinel.cluster.client.DefaultClusterTokenClient;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.log.RecordLog;
import com.alibaba.csp.sentinel.spi.Spi;
import com.alibaba.csp.sentinel.spi.SpiLoaderException;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/8
 * Describe: XXXXXXX.
 */
public final class SpiLoader<S> {
    private static final String SPI_FILE_PREFIX = "META-INF/services/";
    private static final ConcurrentHashMap<String, SpiLoader> SPI_LOADER_MAP = new ConcurrentHashMap();
    private final List<Class<? extends S>> classList = Collections.synchronizedList(new ArrayList());
    private final List<Class<? extends S>> sortedClassList = Collections.synchronizedList(new ArrayList());
    private final ConcurrentHashMap<String, Class<? extends S>> classMap = new ConcurrentHashMap();
    private final ConcurrentHashMap<String, S> singletonMap = new ConcurrentHashMap();
    private final AtomicBoolean loaded = new AtomicBoolean(false);
    private Class<? extends S> defaultClass = null;
    private Class<S> service;

    public static <T> SpiLoader<T> of(Class<T> service) {
        AssertUtil.notNull(service, "SPI class cannot be null");
        AssertUtil.isTrue(service.isInterface() || Modifier.isAbstract(service.getModifiers()), "SPI class[" + service.getName() + "] must be interface or abstract class");
        String className = service.getName();
        SpiLoader<T> spiLoader = (SpiLoader)SPI_LOADER_MAP.get(className);
        if (spiLoader == null) {
            Class var3 = com.alibaba.csp.sentinel.spi.SpiLoader.class;
            Class var4 = com.alibaba.csp.sentinel.spi.SpiLoader.class;
            synchronized(com.alibaba.csp.sentinel.spi.SpiLoader.class) {
                spiLoader = (SpiLoader)SPI_LOADER_MAP.get(className);
                if (spiLoader == null) {
                    SPI_LOADER_MAP.putIfAbsent(className, new SpiLoader(service));
                    spiLoader = (SpiLoader)SPI_LOADER_MAP.get(className);
                }
            }
        }

        return spiLoader;
    }

    static synchronized void resetAndClearAll() {
        Set<Entry<String, SpiLoader>> entries = SPI_LOADER_MAP.entrySet();
        Iterator var1 = entries.iterator();

        while(var1.hasNext()) {
            Entry<String, SpiLoader> entry = (Entry)var1.next();
            SpiLoader spiLoader = (SpiLoader)entry.getValue();
            spiLoader.resetAndClear();
        }

        SPI_LOADER_MAP.clear();
    }

    private SpiLoader(Class<S> service) {
        this.service = service;
    }

    public List<S> loadInstanceList() {
        this.load();
        return this.createInstanceList(this.classList);
    }

    public List<S> loadInstanceListSorted() {
        this.load();
        return this.createInstanceList(this.sortedClassList);
    }

    public S loadHighestPriorityInstance() {
        this.load();
        if (this.sortedClassList.size() == 0) {
            return null;
        } else {
            Class<? extends S> highestClass = (Class)this.sortedClassList.get(0);
            return this.createInstance(highestClass);
        }
    }

    public S loadLowestPriorityInstance() {
        this.load();
        if (this.sortedClassList.size() == 0) {
            return null;
        } else {
            Class<? extends S> lowestClass = (Class)this.sortedClassList.get(this.sortedClassList.size() - 1);
            return this.createInstance(lowestClass);
        }
    }

    public S loadFirstInstance() {
        this.load();
        if (this.classList.size() == 0) {
            return null;
        } else {
            Class<? extends S> serviceClass = (Class)this.classList.get(0);
            S instance = this.createInstance(serviceClass);
            return instance;
        }
    }

    public static Object loadFirstInstanceOrDefault(Class clazz1, Class clazz2) {
        return new DefaultClusterTokenClient();
    }

    public S loadFirstInstanceOrDefault() {
        this.load();
        Iterator var1 = this.classList.iterator();

        Class clazz;
        do {
            if (!var1.hasNext()) {
                return this.loadDefaultInstance();
            }

            clazz = (Class)var1.next();
        } while(this.defaultClass != null && clazz == this.defaultClass);

        return (S) this.createInstance(clazz);
    }

    public S loadDefaultInstance() {
        this.load();
        return this.defaultClass == null ? null : this.createInstance(this.defaultClass);
    }

    public S loadInstance(Class<? extends S> clazz) {
        AssertUtil.notNull(clazz, "SPI class cannot be null");
        if (clazz.equals(this.service)) {
            this.fail(clazz.getName() + " is not subtype of " + this.service.getName());
        }

        this.load();
        if (!this.classMap.containsValue(clazz)) {
            this.fail(clazz.getName() + " is not Provider class of " + this.service.getName() + ",check if it is in the SPI configuration file?");
        }

        return this.createInstance(clazz);
    }

    public S loadInstance(String aliasName) {
        AssertUtil.notEmpty(aliasName, "aliasName cannot be empty");
        this.load();
        Class<? extends S> clazz = (Class)this.classMap.get(aliasName);
        if (clazz == null) {
            this.fail("no Provider class's aliasName is " + aliasName);
        }

        return this.createInstance(clazz);
    }

    synchronized void resetAndClear() {
        SPI_LOADER_MAP.remove(this.service.getName());
        this.classList.clear();
        this.sortedClassList.clear();
        this.classMap.clear();
        this.singletonMap.clear();
        this.defaultClass = null;
        this.loaded.set(false);
    }

    public void load() {
        if (this.loaded.compareAndSet(false, true)) {
            String fullFileName = "META-INF/services/" + this.service.getName();
            ClassLoader classLoader;
            if (SentinelConfig.shouldUseContextClassloader()) {
                classLoader = Thread.currentThread().getContextClassLoader();
            } else {
                classLoader = this.service.getClassLoader();
            }

            if (classLoader == null) {
                classLoader = ClassLoader.getSystemClassLoader();
            }

            Enumeration urls = null;

            try {
                urls = classLoader.getResources(fullFileName);
            } catch (IOException var19) {
                this.fail("Error locating SPI configuration file, filename=" + fullFileName + ", classloader=" + classLoader, var19);
            }

            if (urls != null && urls.hasMoreElements()) {
                label237:
                while(urls.hasMoreElements()) {
                    URL url = (URL)urls.nextElement();
                    InputStream in = null;
                    BufferedReader br = null;

                    try {
                        in = url.openStream();
                        br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

                        while(true) {
                            String line;
                            int commentIndex;
                            do {
                                do {
                                    if ((line = br.readLine()) == null) {
                                        continue label237;
                                    }
                                } while(StringUtil.isBlank(line));

                                line = line.trim();
                                commentIndex = line.indexOf("#");
                            } while(commentIndex == 0);

                            if (commentIndex > 0) {
                                line = line.substring(0, commentIndex);
                            }

                            line = line.trim();
                            Class clazz = null;

                            try {
                                clazz = Class.forName(line, false, classLoader);
                            } catch (ClassNotFoundException var18) {
                                this.fail("class " + line + " not found", var18);
                            }

                            if (!this.service.isAssignableFrom(clazz)) {
                                this.fail("class " + clazz.getName() + "is not subtype of " + this.service.getName() + ",SPI configuration file=" + fullFileName);
                            }

                            this.classList.add(clazz);
                            Spi spi = (Spi)clazz.getAnnotation(Spi.class);
                            String aliasName = spi != null && !"".equals(spi.value()) ? spi.value() : clazz.getName();
                            if (this.classMap.containsKey(aliasName)) {
                                Class<? extends S> existClass = (Class)this.classMap.get(aliasName);
                                this.fail("Found repeat alias name for " + clazz.getName() + " and " + existClass.getName() + ",SPI configuration file=" + fullFileName);
                            }

                            this.classMap.put(aliasName, clazz);
                            if (spi != null && spi.isDefault()) {
                                if (this.defaultClass != null) {
                                    this.fail("Found more than one default Provider, SPI configuration file=" + fullFileName);
                                }

                                this.defaultClass = clazz;
                            }

                            RecordLog.info("[SpiLoader] Found SPI implementation for SPI {}, provider={}, aliasName={}, isSingleton={}, isDefault={}, order={}", new Object[]{this.service.getName(), line, aliasName, spi == null ? true : spi.isSingleton(), spi == null ? false : spi.isDefault(), spi == null ? 0 : spi.order()});
                        }
                    } catch (IOException var20) {
                        this.fail("error reading SPI configuration file", var20);
                    } finally {
                        this.closeResources(in, br);
                    }
                }

                this.sortedClassList.addAll(this.classList);
                Collections.sort(this.sortedClassList, new Comparator<Class<? extends S>>() {
                    public int compare(Class<? extends S> o1, Class<? extends S> o2) {
                        Spi spi1 = (Spi)o1.getAnnotation(Spi.class);
                        int order1 = spi1 == null ? 0 : spi1.order();
                        Spi spi2 = (Spi)o2.getAnnotation(Spi.class);
                        int order2 = spi2 == null ? 0 : spi2.order();
                        return Integer.compare(order1, order2);
                    }
                });
            } else {
                RecordLog.warn("No SPI configuration file, filename=" + fullFileName + ", classloader=" + classLoader, new Object[0]);
            }
        }

    }

    public String toString() {
        return "com.alibaba.csp.sentinel.spi.SpiLoader[" + this.service.getName() + "]";
    }

    private List<S> createInstanceList(List<Class<? extends S>> clazzList) {
        if (clazzList != null && clazzList.size() != 0) {
            List<S> instances = new ArrayList(clazzList.size());
            Iterator var3 = clazzList.iterator();

            while(var3.hasNext()) {
                Class<? extends S> clazz = (Class)var3.next();
                S instance = this.createInstance(clazz);
                instances.add(instance);
            }

            return instances;
        } else {
            return Collections.emptyList();
        }
    }

    private S createInstance(Class<? extends S> clazz) {
        Spi spi = (Spi)clazz.getAnnotation(Spi.class);
        boolean singleton = true;
        if (spi != null) {
            singleton = spi.isSingleton();
        }

        return this.createInstance(clazz, singleton);
    }

    private S createInstance(Class<? extends S> clazz, boolean singleton) {
        Object instance = null;

        try {
            if (singleton) {
                instance = this.singletonMap.get(clazz.getName());
                if (instance == null) {
                    synchronized(this) {
                        instance = this.singletonMap.get(clazz.getName());
                        if (instance == null) {
                            instance = this.service.cast(clazz.newInstance());
                            this.singletonMap.put(clazz.getName(), (S) instance);
                        }
                    }
                }
            } else {
                instance = this.service.cast(clazz.newInstance());
            }
        } catch (Throwable var7) {
            this.fail(clazz.getName() + " could not be instantiated");
        }

        return (S) instance;
    }

    private void closeResources(Closeable... closeables) {
        if (closeables != null && closeables.length != 0) {
            Exception firstException = null;
            Closeable[] var3 = closeables;
            int var4 = closeables.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Closeable closeable = var3[var5];

                try {
                    closeable.close();
                } catch (Exception var8) {
                    if (firstException != null) {
                        firstException = var8;
                    }
                }
            }

            if (firstException != null) {
                this.fail("error closing resources", firstException);
            }
        }

    }

    private void fail(String msg) {
        RecordLog.error(msg, new Object[0]);
        throw new SpiLoaderException("[" + this.service.getName() + "]" + msg);
    }

    private void fail(String msg, Throwable e) {
        RecordLog.error(msg, e);
        throw new SpiLoaderException("[" + this.service.getName() + "]" + msg, e);
    }
}
