package com.fsk.framework.autoconfigure.configuration;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FskBootAutoConfiguration {

    @Bean
    public JSONObject initConfig(){
        JSONObject config = new JSONObject();
        log.info("[#### FskAutoConfiguration.initConfig-----init ####]");
        config.put("FskAutoConfig","initConfig-init");
        log.info("[#### FskAutoConfiguration.initConfig-----end ####]");
        return config;
    }
}
