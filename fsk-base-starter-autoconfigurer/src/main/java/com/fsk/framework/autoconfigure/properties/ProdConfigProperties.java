package com.fsk.framework.autoconfigure.properties;

import com.fsk.framework.autoconfigure.bean.FskDataSourceCenterConfig;
import com.fsk.framework.autoconfigure.bean.FskNacosCenterConfig;
import com.fsk.framework.autoconfigure.bean.sentinel.FskSentinelRuleConfig;
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
 * @since 2022/1/20
 * Describe: XXXXXXX.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "fsk.prod")
public class ProdConfigProperties extends FskBootStarterProperties {

    @NestedConfigurationProperty
    private FskNacosCenterConfig nacos;

    @NestedConfigurationProperty
    private FskDataSourceCenterConfig datasource;

    @NestedConfigurationProperty
    private FskSentinelRuleConfig sentinel;

    @Override
    public FskNacosCenterConfig getFskNacosCenterConfig() {
        return nacos;
    }

    @Override
    public FskDataSourceCenterConfig getFskDataSourceCenterConfig() {
        return datasource;
    }

    @Override
    public FskSentinelRuleConfig getFskSentinelRuleConfig() {
        return sentinel;
    }
}
