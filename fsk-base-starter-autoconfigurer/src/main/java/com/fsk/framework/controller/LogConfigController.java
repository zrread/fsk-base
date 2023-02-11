package com.fsk.framework.controller;

import com.fsk.framework.bean.log.LogbackInfo;
import com.fsk.framework.bean.response.BaseApiResponse;
import com.fsk.framework.utils.LogUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log/config")
@Slf4j
public class LogConfigController {

    @PostMapping("/info")
    @ResponseBody
    public BaseApiResponse<LogbackInfo> info() throws Exception {
        return BaseApiResponse.success(LogUtils.getLogbackInfo());
    }

    @PostMapping("/update")
    @ResponseBody
    public BaseApiResponse<LogbackInfo> update(@RequestBody LogbackInfo logbackInfo) throws Exception {
        BaseApiResponse<LogbackInfo> response = BaseApiResponse.success(LogUtils.updateLogbackInfo(logbackInfo));
        response.setStatusMsg("修改成功");
        return response;
    }

}
