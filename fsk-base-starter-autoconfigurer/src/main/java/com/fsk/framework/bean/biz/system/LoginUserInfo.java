package com.fsk.framework.bean.biz.system;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class LoginUserInfo {
    private String source;//渠道，用户类型
    private String token;
    private String userCode;
    private String userFullName;
    private String userGender;
    private String password;
    private String userTel;
    private String userIdCard;
    private String userStatus;
    private String userOrgCode;
    private String userOrgName;
    private String dataAuthoritySql;
    private String allottedTime;

}
