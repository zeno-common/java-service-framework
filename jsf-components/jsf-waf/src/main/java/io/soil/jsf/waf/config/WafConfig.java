package io.soil.jsf.waf.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.soil.jsf.waf.controller.ProbeController;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static io.soil.jsf.common.date.OffsetDateTimeUtil.OFFSET_DATE_TIME_FORMATTER;

/**
 * WAF（Web Application Framework）配置类，配置 Jackson 序列化/反序列化规则和组件扫描。
 * <p>
 * 主要配置：
 * <ul>
 *   <li>时间格式的序列化/反序列化</li>
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
   * @return 配置好的 Jackson2ObjectMapperBuilderCustomizer
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilder(){

    return builder -> {


      JavaTimeModule javaTimeModule = new JavaTimeModule();
      // 数字精度在 js 中会丢失，所以需要转换为字符串
      javaTimeModule.addSerializer(Long.class, ToStringSerializer.instance);
      javaTimeModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
      javaTimeModule.addSerializer(BigInteger.class, ToStringSerializer.instance);

      // LocalDateTime 序列化/反序列化
      javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(OFFSET_DATE_TIME_FORMATTER));
      javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(OFFSET_DATE_TIME_FORMATTER));
      builder.modules(javaTimeModule);
      builder.timeZone(TimeZone.getDefault());

      // 特性启用
      builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
        DeserializationFeature.READ_ENUMS_USING_TO_STRING);

      // 特性禁用
      builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    };
  }
}
