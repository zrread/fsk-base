package com.fsk.framework.extend.utils.encrypt;

import com.fsk.framework.core.exception.BizException;
import com.fsk.framework.extend.constant.FskConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Slf4j
public class DesCrypto implements ICrypto {

    private static String staticKey = "fushoukang";

    private long dynamicKey;

    public DesCrypto(long dynamicKey) {
        this.dynamicKey = dynamicKey;
    }

    private String getDynamicSecretKey() {
        String key = staticKey + String.valueOf(dynamicKey ^ 1234567890).replaceAll("[13579]", "");
        return key.substring(key.length() - 8);
    }

    @Override
    public String encrypt(String content) {
        try {
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            String originKey = getDynamicSecretKey();
            SecretKeySpec key = new SecretKeySpec(originKey.getBytes(), "DES");

            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encipherByte = cipher.doFinal(contentBytes);
            return Base64.encodeBase64String(encipherByte);
        } catch (Exception e) {
            log.error("签名解密失败！", e);
            throw new BizException(FskConstants.ERROR_STATUS_CODE, "解密错误，请联系负责人", e);
        }

    }

    @Override
    public String decrypt(String base64Str) {
        try {
            byte[] decode = Base64.decodeBase64(base64Str);
            String originKey = getDynamicSecretKey();
            SecretKeySpec key = new SecretKeySpec(originKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decipherByte = cipher.doFinal(decode);
            return new String(decipherByte, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("签名解密失败！", e);
            throw new BizException(FskConstants.ERROR_STATUS_CODE, "签名解密失败！");
        }

    }
}
