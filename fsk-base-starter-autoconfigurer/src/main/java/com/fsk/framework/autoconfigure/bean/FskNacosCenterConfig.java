package com.fsk.framework.autoconfigure.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/1/20
 * Describe: XXXXXXX.
 */
@Getter
@Setter
@Validated
public class FskNacosCenterConfig {
    @NotNull
    private String serverAddr;
    @NotNull
    private String namespace;
    private String username;
    private String password;
    private boolean enabled = true;

    public FskNacosCenterConfig() {
        this.serverAddr = "localhost:8848";
        this.namespace = "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxx";
        this.username = "nacos";
        this.password = "nacos";
    }
}
