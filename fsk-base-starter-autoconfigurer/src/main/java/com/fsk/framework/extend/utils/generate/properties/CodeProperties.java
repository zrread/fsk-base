package com.fsk.framework.extend.utils.generate.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/11
 * Describe: XXXXXXX.
 */
@Component
@ConfigurationProperties(prefix = "fsk.global.code")
public class CodeProperties {

    private boolean enable;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
