package io.soil.util.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.soil.common.date.DateTimeConst;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.fasterxml.jackson.databind.util.StdDateFormat.DATE_FORMAT_STR_ISO8601;

/**
 * JSON 映射工具类，封装 Jackson {@link ObjectMapper} 提供对象与 JSON 之间的序列化/反序列化操作。
 * <p>
 * 内置配置了 Java 8 时间模块、枚举字符串序列化等特性。
 * </p>
 *
 * @author zeno.w
 * @date 2021/12/9
 */
@Slf4j
public final class JsonMapper {

  public static final ObjectMapper OBJECT_MAPPER;

  static {
    OBJECT_MAPPER = new ObjectMapper();
    config();
  }

  private static void config() {

    // 时间格式配置
    JavaTimeModule javaTimeModule = new JavaTimeModule();

    // OffsetDateTime 序列化/反序列化（带时区，使用 ISO 8601 格式，保留原始偏移量）
    javaTimeModule.addSerializer(new OffsetDateTimeIsoSerializer());
    javaTimeModule.addDeserializer(OffsetDateTime.class, new OffsetDateTimeIsoDeserializer());

    OBJECT_MAPPER.registerModule(javaTimeModule);

    OBJECT_MAPPER.setDateFormat(DateTimeConst.ISO8601_FORMAT);

    // 特性启用
    OBJECT_MAPPER.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
    OBJECT_MAPPER.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

    // 特性禁用
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
  }

  private JsonMapper() {}

  /**
   * Java 对象转换成 json 字符串
   *
   * @param object 转换对象
   * @return json 字符串
   */
  public static String toJsonString(Object object) {

    if (!Objects.isNull(object)) {
      try {
        return OBJECT_MAPPER.writeValueAsString(object);
      } catch (JsonProcessingException e) {
        log.error("java 对象转换成 json 字符串发生异常：" + object, e);
      }
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

  /**
   * OffsetDateTime ISO 8601 序列化器，保留原始时区偏移量
   */
  private static class OffsetDateTimeIsoSerializer extends StdSerializer<OffsetDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    OffsetDateTimeIsoSerializer() {
      super(OffsetDateTime.class);
    }

    @Override
    public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
      gen.writeString(FORMATTER.format(value));
    }
  }

  /**
   * OffsetDateTime ISO 8601 反序列化器，保留原始时区偏移量
   */
  private static class OffsetDateTimeIsoDeserializer extends JsonDeserializer<OffsetDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      return OffsetDateTime.parse(p.getText(), FORMATTER);
    }
  }
}
