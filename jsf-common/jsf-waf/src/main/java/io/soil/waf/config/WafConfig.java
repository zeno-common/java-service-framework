package io.soil.waf.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.soil.common.constant.TimeFormatConstant;
import io.soil.waf.controller.ProbeController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;

@Configuration
@ComponentScan(basePackageClasses = {ProbeController.class})
public class WafConfig {


  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(){

    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

    // 时间格式
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(TimeFormatConstant.DATE_TIME_FORMAT);
    LocalDateTimeSerializer ldtSerializer = new LocalDateTimeSerializer(dateTimeFormatter);
    builder.serializers(ldtSerializer);

    LocalDateTimeDeserializer ldtDeserializer = new LocalDateTimeDeserializer(dateTimeFormatter);
    builder.deserializers(ldtDeserializer);
    builder.simpleDateFormat(TimeFormatConstant.DATE_TIME_FORMAT);

    // 特性启用
    builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
                             DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    // 特性禁用
    builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    return builder;
  }
}
