package com.fsk.framework.autoconfigure.login;

import lombok.Getter;
import lombok.Setter;
/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/23
 * Describe: CustomTokenProperties.
 */
@Getter
@Setter
public class CustomToken {
    private String name;

    public CustomToken(String name) {
        this.name = name;
    }
}
