package com.fsk.framework.extend.utils.encrypt;

public interface ICrypto {

    String encrypt(String content);

    String decrypt(String base64Str);
}
