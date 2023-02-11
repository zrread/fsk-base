package com.fsk.framework.autoconfigure.properties;

import com.fsk.framework.autoconfigure.bean.FskDataSourceCenterConfig;
import com.fsk.framework.autoconfigure.bean.FskNacosCenterConfig;
import com.fsk.framework.autoconfigure.bean.sentinel.FskSentinelRuleConfig;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class FskBootStarterProperties {
    public abstract FskNacosCenterConfig getFskNacosCenterConfig();
    public abstract FskDataSourceCenterConfig getFskDataSourceCenterConfig();
    public abstract FskSentinelRuleConfig getFskSentinelRuleConfig();
}
