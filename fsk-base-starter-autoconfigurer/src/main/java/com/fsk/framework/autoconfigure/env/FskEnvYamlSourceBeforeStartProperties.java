package com.fsk.framework.autoconfigure.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/12
 * Describe: FskEnvYamlSourceBeforeStartProperties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "fsk.env")
public class FskEnvYamlSourceBeforeStartProperties {
    private String yamlPath;
}