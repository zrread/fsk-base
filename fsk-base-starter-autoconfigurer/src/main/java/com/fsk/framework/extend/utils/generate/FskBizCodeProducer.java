package com.fsk.framework.extend.utils.generate;

import com.fsk.framework.alarm.AlarmHelper;
import com.fsk.framework.alarm.AlarmTypeStandard;
import com.fsk.framework.constants.ExceptionConstant;
import com.fsk.framework.core.exception.BizException;
import com.fsk.framework.core.exception.ResponseRetEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * BelongName Fsk Producer
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/10
 * Describe: Biz Code Producer.
 */
@Slf4j
public final class FskBizCodeProducer implements ExceptionConstant {

    public static String producer(String prefix) throws Exception {
        if (StringUtils.isBlank(prefix)) {
            log.error(AlarmHelper.toAlarmMsg(AlarmTypeStandard.ALARM_TYPE_STANDARD_ERROR, EXCE_MSG_6), prefix);
            throw new BizException(ResponseRetEnum.BIZ_EXCEPTION_PARAMS_ILLEGAL.getCode(), EXCE_MSG_5);
        }
        return CoderUpgradeHelper.incr(prefix);
    }
}
