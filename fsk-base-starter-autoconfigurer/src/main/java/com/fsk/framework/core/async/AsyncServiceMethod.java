package com.fsk.framework.core.async;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsyncServiceMethod {
    private String method;

    public AsyncServiceMethod(String method) {
        this.method = method;
    }
}
