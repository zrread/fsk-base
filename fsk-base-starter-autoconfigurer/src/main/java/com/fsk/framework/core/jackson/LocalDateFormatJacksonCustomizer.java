package com.fsk.framework.core.jackson;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * BelongName Fsk App
 * Copyright com.fsk
 * @version 1.0.0
 * @author Gary
 * @since 2022/8/2
 * Describe: format for LocalDateTime with jackson.
 */
@Configuration
@ConditionalOnProperty(prefix = "fsk.global.localdatetime-format", value = "enable", havingValue = "true", matchIfMissing = true)
public class LocalDateFormatJacksonCustomizer implements Ordered {

    @Value("${spring.jackson.date-format}")
    private String pattern;

    private final String pattern_receive = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeSerializer() {
        final LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
        return jackson2ObjectMapperBuilder -> jackson2ObjectMapperBuilder.serializerByType(LocalDateTime.class, localDateTimeSerializer);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer localDateTimeDeserializer() {
        final LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(pattern_receive));
        return jackson2ObjectMapperBuilder -> jackson2ObjectMapperBuilder.deserializerByType(LocalDateTime.class, localDateTimeDeserializer);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
