package io.soil.waf.config;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.OffsetDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

import io.soil.common.date.DateTimeUtil;
import io.soil.common.date.TimeFormatConstant;
import io.soil.waf.controller.ProbeController;

import java.time.OffsetDateTime;

/**
 * WAF（Web Application Framework）配置类，配置 Jackson 序列化/反序列化规则和组件扫描。
 * <p>
 * 主要配置：
 * <ul>
 *   <li>Java 8 时间格式的序列化/反序列化</li>
 *   <li>枚举使用字符串序列化</li>
 *   <li>忽略未知属性</li>
 * </ul>
 * </p>
 *
 * @author zeno
 */
@Configuration
@ComponentScan(basePackageClasses = {ProbeController.class})
public class WafConfig {


  /**
   * 配置 Jackson ObjectMapper 构建器，统一 JSON 序列化/反序列化规则
   *
   * @return 配置好的 Jackson2ObjectMapperBuilder
   */
  @Bean
  public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(){

    Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

    // LocalDateTime 序列化（无时区）
    DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern(TimeFormatConstant.DATE_TIME_FORMAT);
    builder.serializers(new LocalDateTimeSerializer(localDateTimeFormatter));

    // OffsetDateTime 序列化/反序列化（带时区）
    DateTimeFormatter offsetDateTimeFormatter = DateTimeUtil.ISO_OFFSET_FORMATTER;
    builder.serializers(new OffsetDateTimeSerializer(offsetDateTimeFormatter));
    builder.deserializers(new OffsetDateTimeDeserializer(offsetDateTimeFormatter));
    builder.simpleDateFormat(TimeFormatConstant.ISO_DATE_TIME_OFFSET_FORMAT);

    // 特性启用
    builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
                             DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    // 特性禁用
    builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    return builder;
  }
}
