package com.fsk.framework.autoconfigure.bean;

import lombok.Getter;
import lombok.Setter;

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
public class FskDataSourceCenterConfig {
    private String username;
    private String password;
    private String url;

    public FskDataSourceCenterConfig() {
        this.username = "root";
        this.password = "fsk@123456";
        this.url = "jdbc:mysql://localhost:3306";
    }
}
