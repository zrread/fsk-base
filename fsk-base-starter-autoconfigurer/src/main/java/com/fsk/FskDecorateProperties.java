package com.fsk;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/1/11
 * Describe: FskDecorateProperties.
 */
@Getter
@Setter
public class FskDecorateProperties {
    private String yamlSource = "";
    private JSONObject vars = new JSONObject();
    private ArrayList<Object> obs = new ArrayList<>();
    private HashMap<String, String> mas = new HashMap<>();
    private HashMap<String, Object> mas1 = new HashMap<>();

    public FskDecorateProperties() {
    }

    public FskDecorateProxy build() {
        FskDecorateProxy proxy = new FskDecorateProxy();
        proxy.setPre(this);
        return proxy;
    }
}
