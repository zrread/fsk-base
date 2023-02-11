package com.fsk.framework.processer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/12
 * Describe: Customize the Environment or ApplicationContext Before It Starts.
 * Warning:
 * The Environment has already been prepared with all the usual property sources that Spring Boot loads by default.
 * It is therefore possible to get the location of the file from the environment. The preceding example adds the
 * custom-resource property source at the end of the list so that a key defined in any of the usual other locations
 * takes precedence. A custom implementation may define another order.
 */
@Slf4j
public class FskEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final YamlPropertySourceLoader loader = new YamlPropertySourceLoader();

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Resource path = new ClassPathResource("customize.yml");
        this.loadYaml(environment, path);
    }

    private void loadYaml(ConfigurableEnvironment environment, Resource path) {
        if (path.exists()) {
            try {
                environment.getPropertySources().addLast(this.loader.load("custom-resource", path).get(0));
            } catch (IOException ex) {
                throw new IllegalStateException("Failed to load yaml configuration from " + path, ex);
            }
        } else {
            log.info("Resource " + path + " is not configured");
        }
    }

}
