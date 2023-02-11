package com.fsk.framework.extend.utils.generate;

public enum BizCodePrefixEnum {

    PREFIX_SYSTEM_CODE("1", "系统code前缀", 8),
    ;

    String CODE;
    String DESC;
    int LENGTH;

    BizCodePrefixEnum() {
    }

    BizCodePrefixEnum(String CODE, String DESC, int LENGTH) {
        this.CODE = CODE;
        this.DESC = DESC;
        this.LENGTH = LENGTH;
    }

}
