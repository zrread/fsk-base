package com.fsk;

import com.fsk.framework.annotation.FskBootExtend;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * BelongName Fsk AbstractApp
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022-01-11 10:52:00
 * Describe: All boot classes must integrate this class.
 */
public abstract class AbstractApp {

    @FskBootExtend
    abstract FskDecorateProxy decorate(@Autowired FskDecorateProperties properties);

    public FskDecorateProxy initProxy() {
        return this.decorate(new FskDecorateProperties());
    }
}
