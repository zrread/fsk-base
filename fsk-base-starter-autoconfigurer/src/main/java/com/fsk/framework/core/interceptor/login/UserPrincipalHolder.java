package com.fsk.framework.core.interceptor.login;

import com.fsk.framework.bean.biz.system.LoginUserInfo;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/2/25
 * Describe: User Principal Holder.
 */
public final class UserPrincipalHolder {

    public static LoginUserInfo get(){
        return FskUserPrincipalProcessor.obtain();
    }

}
