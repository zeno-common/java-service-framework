package io.soil.waf.config;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

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

    // 特性启用
    builder.featuresToEnable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING,
                             DeserializationFeature.READ_ENUMS_USING_TO_STRING);

    // 特性禁用
    builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                              SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return builder;
  }

  /**
   * OffsetDateTime ISO 8601 序列化器，保留原始时区偏移量
   */
  private static class OffsetDateTimeIsoSerializer extends StdSerializer<OffsetDateTime> {

    OffsetDateTimeIsoSerializer() {
      super(OffsetDateTime.class);
    }

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value));
    }
  }

  /**
   * OffsetDateTime ISO 8601 反序列化器，保留原始时区偏移量
   */
  private static class OffsetDateTimeIsoDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return OffsetDateTime.parse(p.getText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
  }
}
