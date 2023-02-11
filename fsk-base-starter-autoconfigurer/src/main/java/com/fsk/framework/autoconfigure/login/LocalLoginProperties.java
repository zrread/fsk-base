package com.fsk.framework.autoconfigure.login;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/23
 * Describe: LocalLoginProperties.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "fsk.global.local-user")
public class LocalLoginProperties {

    private boolean enable;

    @NestedConfigurationProperty
    private CustomToken customToken = new CustomToken("NULL");
}
