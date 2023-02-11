package com.fsk.framework.extend.constant;

/**
 * 常量类
 * @author xiel 2018年9月6日
 */
public class FskConstants {
  
  private FskConstants() {
  }

  public static final String TOKEN_KEY = "token";
  public static final String SOURCE_KEY = "source";
  public static final String REQUEST_UUID = "X-Request-ID";
  public static final String GRAY_HEADER = "A-Gray-Header";
  public static final String ERROR_STATUS_CODE = "-200";

  // 跨网关无token调用相关常量
  public static final String SOURCE_VALUE_TIMED_TASK = "6010";
  public static final String SIGN_CONTENT = "fsk";

}
