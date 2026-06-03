package io.soil.waf.config;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.soil.common.constant.TimeFormatConstant;
import io.soil.waf.controller.ProbeController;

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
