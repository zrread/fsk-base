package com.fsk.framework.extend.utils.generate;

import com.alibaba.fastjson.JSONObject;
import com.fsk.FskSpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.BAD_PARAM;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/28
 * Describe: CoderUpgradeHelper.
 */
@Slf4j
public class CoderUpgradeHelper {

    protected static String incr(String key) throws Exception {
        CoderTemplateResource coderJedisResource = FskSpringContextHolder.getBeanByClass(CoderTemplateResource.class);
        String codeKey = coderJedisResource.getAPP_NAME() + key;
        log.info("[CoderUpgradeHelper.incr.bizCodeKey={},app={}]", codeKey, coderJedisResource.getAPP_NAME());
        Object res = CoderTemplateResource.get(codeKey);
        log.info("[CoderUpgradeHelper.incr.bizCodeKey={},value={},app={}]", codeKey, JSONObject.toJSONString(res), coderJedisResource.getAPP_NAME());
        try {
            if (null != res) {
                Long aLong = Long.valueOf(JSONObject.toJSONString(res));
            }
            Long var = CoderTemplateResource.increment(codeKey);
            log.info("[CoderUpgradeHelper.incr.lasted.var={},time={},app={}]", var, System.currentTimeMillis(), coderJedisResource.getAPP_NAME());
            return CoderJedisResource.zeroFill(var, 8);
        } catch (Exception e) {
            log.error("[CoderUpgradeHelper.incr.Exception={},app={}]", e, coderJedisResource.getAPP_NAME());
            throw new Exception("Redis code value Illegal:" + res, new BAD_PARAM("Redis code value Illegal:" + res));
        }
    }
}
