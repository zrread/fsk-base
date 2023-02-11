package com.fsk.framework.core.exception;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/3/23
 * Describe: ResObject.
 */
public class ResObject<T> extends BaseSystemRet {
    private T customBean;

    public T getCustomBean() {
        return customBean;
    }

    public void setCustomBean(T customBean) {
        this.customBean = customBean;
    }
}
