package io.soil.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.soil.common.constant.TimeFormatConstant;
import io.soil.common.date.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author wangzezhou
 * @date 2021/12/9
 */
@Slf4j
public final class JsonMapper {

  public static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper();
    config(OBJECT_MAPPER);
  }

  private static void config(ObjectMapper objectMapper) {

    // 时间格式配置
    JavaTimeModule javaTimeModule = new JavaTimeModule();
    DateTimeFormatter dateTimeFormatter = DateTimeUtil.DEFAULT_FORMATTER;
    LocalDateTimeSerializer ldtSerializer = new LocalDateTimeSerializer(dateTimeFormatter);
    javaTimeModule.addSerializer(ldtSerializer);

    LocalDateTimeDeserializer ldtDeserializer = new LocalDateTimeDeserializer(dateTimeFormatter);
    javaTimeModule.addDeserializer(LocalDateTime.class, ldtDeserializer);

    objectMapper.registerModule(javaTimeModule);
    objectMapper.setDateFormat(new SimpleDateFormat(TimeFormatConstant.DATE_TIME_FORMAT));

    // 特性启用
    objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
    objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

    // 特性禁用
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private JsonMapper() {
  }

  /**
   * Java 对象转换成 json 字符串
   *
   * @param object
   * @return json 字符串
   */
  public static String toJsonString(Object object) {

    try {
      return OBJECT_MAPPER.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("java 对象转换成 json 字符串发生异常：" + object, e);
    }

    return "{}";
  }


  /**
   * java 对象转换成 JsonNode 对象
   *
   * @param object java 对象
   * @return JsonNode 对象
   */
  public static JsonNode toJsonNode(Object object) {
    return OBJECT_MAPPER.valueToTree(object);
  }

  /**
   * json 字符串转成 JsonNode 对象
   *
   * @param jsonContent json 字符串
   * @return JsonNode 对象
   */
  public static JsonNode toJsonNode(String jsonContent) {
    try {
      return OBJECT_MAPPER.readTree(jsonContent);
    } catch (JsonProcessingException e) {
      log.error("json 字符串转换成 JsonNode 对象异常：" + jsonContent, e);
    }

    return NullNode.getInstance();
  }

  /**
   * json 字符串转成 java 对象
   *
   * @param jsonContent json 字符串
   * @param clazz       java Class
   * @return java 对象
   */
  public static <T> T toObject(String jsonContent, Class<T> clazz) {

    if (Objects.isNull(jsonContent)) {
      return null;
    }

    try {
      return OBJECT_MAPPER.readValue(jsonContent, clazz);
    } catch (JsonProcessingException e) {
      log.error("json 字符串转换成 java 对象发生异常：" + jsonContent, e);
    }

    return null;
  }

  /**
   * 字符输入流对象转成 java 对象
   *
   * @param in    字符输入流对象
   * @param clazz java Class
   * @return java 对象
   */
  public static <T> T toObject(InputStream in, Class<T> clazz) {

    if (Objects.isNull(in)) {
      return null;
    }

    try {
      return OBJECT_MAPPER.readValue(in, clazz);
    } catch (IOException e) {
      log.error("字符输入流对象转成 java 对象发生异常" + clazz, e);
    }
    return null;
  }

  /**
   * 字符输入流对象转成 java 对象
   *
   * @param jsonContent 字符输入流对象
   * @param typeRef     java 类引用
   * @return java 对象
   */
  public static <T> T toObject(String jsonContent, TypeReference<T> typeRef) {

    if (Objects.isNull(jsonContent)) {
      return null;
    }

    try {
      return OBJECT_MAPPER.readValue(jsonContent, typeRef);
    } catch (IOException e) {
      log.error("输入流对象转成 java 对象发生异常：" + typeRef.getType().getClass(), e);
    }
    return null;
  }

  /**
   * JsonNode 对象转换成 java对象
   *
   * @param value JsonNode 对象
   * @param clazz java Class
   * @param <T>   要转换的对象类型
   * @return java 对象
   */
  public static <T> T toObject(JsonNode value, Class<T> clazz) {
    return OBJECT_MAPPER.convertValue(value, clazz);
  }

  /**
   * 字符输入流对象转成 java 对象
   *
   * @param in      字符输入流对象
   * @param typeRef java 类引用
   * @return java 对象
   */
  public static <T> T toObject(InputStream in, TypeReference<T> typeRef) {

    if (Objects.isNull(in)) {
      return null;
    }

    try {
      return OBJECT_MAPPER.readValue(in, typeRef);
    } catch (IOException e) {
      log.error("输入流对象转成 java 对象发生异常：" + typeRef.getType().getClass(), e);
    }
    return null;
  }
}
