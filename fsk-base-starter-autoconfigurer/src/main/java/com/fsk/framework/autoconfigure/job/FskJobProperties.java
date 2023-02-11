package com.fsk.framework.autoconfigure.job;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/9
 * Describe: XXXXXXX.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "fsk.global.job")
public class FskJobProperties {
    private boolean enable;
}
