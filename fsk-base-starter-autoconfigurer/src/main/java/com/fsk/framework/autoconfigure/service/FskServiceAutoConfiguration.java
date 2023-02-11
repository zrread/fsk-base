package com.fsk.framework.autoconfigure.service;

import com.fsk.framework.autoconfigure.properties.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        OfficeConfigProperties.class,
        DevConfigProperties.class,
        TestConfigProperties.class,
        UatConfigProperties.class,
        ProdConfigProperties.class
})
public class FskServiceAutoConfiguration {

    @Autowired
    private OfficeConfigProperties officeConfigProperties;

    @Autowired
    private DevConfigProperties devConfigProperties;

    @Autowired
    private TestConfigProperties testConfigProperties;

    @Autowired
    private UatConfigProperties uatConfigProperties;

    @Autowired
    private ProdConfigProperties prodConfigProperties;

    @Value("${spring.profiles.active}")
    private String ACTIVE;

    @Bean
    public FskBootService initFskBootNacosService(){
        FskBootService fskBootService = new FskBootService();
        if(StringUtils.equals(ACTIVE,"office")){
            fskBootService.setFskBootStarterProperties(officeConfigProperties);
        }else if(StringUtils.equals(ACTIVE,"dev")){
            fskBootService.setFskBootStarterProperties(devConfigProperties);
        }else if(StringUtils.equals(ACTIVE,"test")){
            fskBootService.setFskBootStarterProperties(testConfigProperties);
        }else if(StringUtils.equals(ACTIVE,"uat")){
            fskBootService.setFskBootStarterProperties(uatConfigProperties);
        }else if(StringUtils.equals(ACTIVE,"prod")) {
            fskBootService.setFskBootStarterProperties(prodConfigProperties);
        }

        return fskBootService;
    }


}
