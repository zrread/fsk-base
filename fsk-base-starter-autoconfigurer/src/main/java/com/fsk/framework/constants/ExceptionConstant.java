package com.fsk.framework.constants;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/25
 * Describe: ExceptionConstant.
 */
public interface ExceptionConstant {

    String EXCE_MSG_0 = "The user principal information is empty !";

    String EXCE_MSG_1 = "Failed to obtain user principal information: The possible cause is that the session Redis connection fails or the session Redis configuration is missing. Make sure you have configured session Redis information.";

    String EXCE_MSG_2 = "The value of ${fsk.global.local-user.enable} must be set to true !";

    String EXCE_MSG_3 = "jedisPool getResource() error occurred!";

    String EXCE_MSG_4 = "The login token is empty !";

    String EXCE_MSG_5 = "BizCode prefix can not be blank for producing code!";

    String EXCE_MSG_6 = "BizCode prefix is blank:{}";

    String EXCE_MSG_7 = "The current request is not an HTTP request";

    String EXCE_MSG_8 = "BeforeBodyWrite is found";


}
