package com.fsk.framework.source.datasource;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosPropertySourceRepository;
import com.alibaba.cloud.nacos.client.NacosPropertySource;
import com.fsk.framework.translation.core.LocatorTranslation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.stream.Collectors;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/10/10
 * Describe: Datasource Locator Translation.
 */
@Slf4j
public class DatasourceLocatorTranslation implements LocatorTranslation {

    private NacosConfigProperties nacosConfigProperties;
    private NacosConfigManager nacosConfigManager;

    @Autowired
    public DatasourceLocatorTranslation(NacosConfigProperties nacosConfigProperties, NacosConfigManager nacosConfigManager) {
        this.nacosConfigProperties = nacosConfigProperties;
        this.nacosConfigManager = nacosConfigManager;
    }

    @Override
    public void translation(Environment environment, Map<String, Object> source) {
        String active = environment.getProperty("spring.profiles.active");
        String appName = environment.getProperty("spring.application.name");

        String dataId = appName + "-" + active + "." + nacosConfigProperties.getFileExtension();
        String group = nacosConfigProperties.getGroup();
        NacosPropertySource nacosPropertySource = NacosPropertySourceRepository.getNacosPropertySource(dataId, group);
        Map<String, Object> nacosPropertySourceSource = nacosPropertySource.getSource();

        // datasource prefix
        String prefix = "fsk." + active + ".datasource";
        String url = "fsk." + active + ".datasource.url";
        String username = "fsk." + active + ".datasource.username";
        String password = "fsk." + active + ".datasource.password";

        if (Objects.nonNull(nacosPropertySourceSource.get(url)) &&
                Objects.nonNull(nacosPropertySourceSource.get(username)) &&
                Objects.nonNull(nacosPropertySourceSource.get(password))) {
            // 默认初始值
            source.put("spring.datasource.type", "com.zaxxer.hikari.HikariDataSource");
            source.put("spring.datasource.hikari.driver-class-name", "com.mysql.cj.jdbc.Driver");
            source.put("spring.datasource.hikari.pool-name", appName + "-hikari-pool");
            source.put("spring.datasource.hikari.minimum-idle", 100);
            source.put("spring.datasource.hikari.idle-timeout", 200000);
            source.put("spring.datasource.hikari.maximum-pool-size", 100);
            source.put("spring.datasource.hikari.auto-commit", true);
            source.put("spring.datasource.hikari.max-lifetime", 300000);
            source.put("spring.datasource.hikari.connection-timeout", 30000);
            source.put("spring.datasource.hikari.validation-timeout", 5000);

        }

        // replace
        Set<String> keys = nacosPropertySourceSource.keySet();
        List<String> collect = keys.stream().filter(key -> key.contains(prefix)).collect(Collectors.toList());
        collect.forEach((item) -> {
            String value = nacosPropertySourceSource.get(item).toString();
            String springKey = item.replace("fsk." + active, "spring");
            source.put(springKey, value);
        });
    }
}
