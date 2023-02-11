package com.fsk.framework.endpoint;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Configuration
@Endpoint(id = "fsk-library")
@Slf4j
public class FskLibraryEndpoint {
    private static final String RESOURCE_PATTERN = "/META-INF/**/pom.properties";
    private static final String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + RESOURCE_PATTERN;
    private static Resource[] resources;
    private static final Map<String, String> map = new HashMap<>();

    @ReadOperation
    public Map<String, String> getLibraryEndpoint() {
        try {
            if (CollectionUtils.isEmpty(map) && (Objects.isNull(resources) || resources.length == 0)) {
                resources = new PathMatchingResourcePatternResolver().getResources(pattern);
                for (Resource resource : resources) {
                    Properties properties = PropertiesLoaderUtils.loadProperties(resource);
                    String artifactId = String.valueOf(properties.get("artifactId"));
                    if (artifactId.contains("fsk")) {
                        map.put(artifactId, String.valueOf(properties.get("version")));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("---------- getLibraryEndpoint exception:{}", e.getMessage(), e);
        }
        return map;
    }

}
