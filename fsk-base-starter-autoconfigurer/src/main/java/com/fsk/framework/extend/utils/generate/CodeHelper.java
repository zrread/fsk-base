package com.fsk.framework.extend.utils.generate;

import com.fsk.FskSpringContextHolder;
import com.fsk.framework.constants.CommonContanst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.omg.CORBA.BAD_PARAM;
import redis.clients.jedis.Jedis;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/10
 * Describe: CodeHelper.
 */
@Slf4j
@Deprecated
public class CodeHelper implements CommonContanst {

    protected static String incr(String key) throws Exception {
        CoderJedisResource coderJedisResource = FskSpringContextHolder.getBeanByClass(CoderJedisResource.class);
        String codeKey = coderJedisResource.getAPP_NAME() + key;
        log.info("[CodeHelper.incr.bizCodeKey={},app={}]", codeKey, coderJedisResource.getAPP_NAME());
        Jedis resource = CoderJedisResource.getResource();
        String res = resource.get(codeKey);
        log.info("[CodeHelper.incr.resource.get:codeKey={},value={},app={}]", codeKey, res, coderJedisResource.getAPP_NAME());
        try {
            if (StringUtils.isNotBlank(res)) {
                Long aLong = Long.valueOf(res);
            }
            Long var = resource.incrBy(codeKey,1L);
            log.info("[CodeHelper.incr.lasted.var={},time={},app={}]", var, System.currentTimeMillis(), coderJedisResource.getAPP_NAME());
            return CoderJedisResource.zeroFill(var, 8);
        } catch (Exception e) {
            log.error("[CodeHelper.incr.Exception={},app={}]", e, coderJedisResource.getAPP_NAME());
            throw new Exception("Redis code value Illegal:" + res, new BAD_PARAM("Redis code value Illegal:" + res));
        }finally {
            resource.close();
        }
    }
}
