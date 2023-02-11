package com.fsk.framework.metrics;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


@Configuration
public class AppRequestMetrics {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> {
            registry.config().meterFilter(MeterFilter.ignoreTags("outcome","exception")).meterFilter(
                    new MeterFilter() {
                        @Override
                        public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
                            if (id.getType() == Meter.Type.TIMER && id.getName().matches("^(http|hystrix){1}.*")) {
                                return DistributionStatisticConfig.builder()
                                        .percentilesHistogram(true)
                                        .percentiles(0.5, 0.90, 0.95, 0.99)
                                        .serviceLevelObjectives(
                                                Duration.ofMillis(200).toNanos(),
                                                Duration.ofMillis(500).toNanos(),
                                                Duration.ofSeconds(1).toNanos(),
                                                Duration.ofMillis(1500).toNanos(),
                                                Duration.ofSeconds(3).toNanos(),
                                                Duration.ofSeconds(6).toNanos())
                                        .minimumExpectedValue(1d)
                                        .maximumExpectedValue(6d)
                                        .build()
                                        .merge(config);
                            } else {
                                return config;
                            }
                        }
                    });
        };
    }

}

