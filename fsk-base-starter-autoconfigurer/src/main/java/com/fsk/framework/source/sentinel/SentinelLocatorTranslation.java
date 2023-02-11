package com.fsk.framework.source.sentinel;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.fsk.framework.pubconfig.entity.KeyEntities;
import com.fsk.framework.pubconfig.entity.SentinelSourceEntity;
import com.fsk.framework.translation.core.LocatorTranslation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/10/10
 * Describe: Sentinel Locator Translation.
 */
@Slf4j
public class SentinelLocatorTranslation implements LocatorTranslation {

    private KeyEntities keyEntities;

    @Autowired
    public SentinelLocatorTranslation(KeyEntities keyEntities) {
        this.keyEntities = keyEntities;
    }

    @Override
    public void translation(Environment environment, Map<String, Object> source) {
        SentinelSourceEntity sentinelSource = keyEntities.getSentinelSource();
        if (sentinelSource == null ||
                StringUtils.isBlank(sentinelSource.getTransport().getDashboard()) ||
                StringUtils.isBlank(sentinelSource.getTransport().getPort())
        ) {
            log.warn("Sentinel is not configured, Check the configuration of the public space!");
        } else {
            SentinelProperties.Transport transport = sentinelSource.getTransport();
            source.put("spring.cloud.sentinel.transport.dashboard", transport.getDashboard());
            source.put("spring.cloud.sentinel.transport.port", transport.getPort());
            if (StringUtils.isNotBlank(transport.getHeartbeatIntervalMs())) {
                source.put("spring.cloud.sentinel.transport.heartbeatIntervalMs", transport.getHeartbeatIntervalMs());
            }
            if (StringUtils.isNotBlank(transport.getClientIp())) {
                source.put("spring.cloud.sentinel.transport.clientIp", transport.getClientIp());
            }
            source.put("spring.cloud.sentinel.eager", sentinelSource.isEager());
            source.put("spring.cloud.sentinel.enabled", sentinelSource.isEnable());
        }
    }
}
