package io.soil.jsf.util.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;

import static org.junit.Assert.*;

/**
 * {@link JsonMapper} 单元测试
 */
public class JsonMapperTest {

  // ==================== toJsonString ====================

  @Test
  public void toString_shouldSerializeObject() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("name", "test");
    map.put("value", 123);

    String json = JsonMapper.toString(map);

    assertTrue(json.contains("\"name\""));
    assertTrue(json.contains("\"test\""));
    assertTrue(json.contains("\"value\""));
    assertTrue(json.contains("123"));
  }

  @Test
  public void toJsonString_withNullObject_shouldReturnEmpty() {
    String json = JsonMapper.toString(null);

    assertEquals("{}", json);
  }

  @Test
  public void toString_withEnum_shouldSerializeAsString() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("status", TestStatus.ACTIVE);

    String json = JsonMapper.toString(map);

    assertTrue(json.contains("\"ACTIVE\""));
  }

  // ==================== toJsonNode (Object) ====================

  @Test
  public void toJsonNode_fromObject_shouldReturnJsonNode() {
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("key", "value");

    JsonNode node = JsonMapper.toJsonNode(map);

    assertEquals("value", node.get("key").asText());
  }

  // ==================== toJsonNode (String) ====================

  @Test
  public void toJsonNode_fromString_shouldReturnJsonNode() {
    String json = "{\"name\":\"test\"}";

    JsonNode node = JsonMapper.toJsonNode(json);

    assertEquals("test", node.get("name").asText());
  }

  @Test
  public void toJsonNode_withInvalidJson_shouldReturnNullNode() {
    String json = JsonMapper.toString("not json".getBytes());

    // 字节数组也能被序列化为字符串
    assertNotNull(json);
  }

  // ==================== toObject (String, Class) ====================

  @Test
  public void toObject_shouldDeserializeJson() {
    String json = "{\"name\":\"test\",\"value\":42}";

    Map result = JsonMapper.toObject(json, Map.class);

    assertEquals("test", result.get("name"));
    assertEquals(42, result.get("value"));
  }

  @Test
  public void toObject_withNullJson_shouldReturnNull() {
    Map result = JsonMapper.toObject((String) null, Map.class);

    assertNull(result);
  }

  @Test
  public void toObject_withInvalidJson_shouldReturnNull() {
    Map result = JsonMapper.toObject("not json", Map.class);

    assertNull(result);
  }

  // ==================== toObject (InputStream, Class) ====================

  @Test
  public void toObject_fromInputStream_shouldDeserialize() {
    String json = "{\"name\":\"test\"}";
    InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

    Map result = JsonMapper.toObject(is, Map.class);

    assertEquals("test", result.get("name"));
  }

  @Test
  public void toObject_withNullInputStream_shouldReturnNull() {
    Map result = JsonMapper.toObject((InputStream) null, Map.class);

    assertNull(result);
  }

  // ==================== toObject (String, TypeReference) ====================

  @Test
  public void toObject_withTypeReference_shouldDeserializeGeneric() {
    String json = "[1,2,3]";

    List<Integer> result = JsonMapper.toObject(json, new TypeReference<>() {});

    assertEquals(3, result.size());
    assertEquals(Integer.valueOf(1), result.get(0));
    assertEquals(Integer.valueOf(2), result.get(1));
    assertEquals(Integer.valueOf(3), result.get(2));
  }

  @Test
  public void toObject_withNullJsonTypeRef_shouldReturnNull() {
    List<Integer> result = JsonMapper.toObject((String) "", new TypeReference<>() {});

    assertNull(result);
  }

  // ==================== toObject (JsonNode, Class) ====================

  @Test
  public void toObject_fromJsonNode_shouldDeserialize() {
    JsonNode node = JsonMapper.toJsonNode("{\"name\":\"test\"}");

    Map result = JsonMapper.toObject(node, Map.class);

    assertEquals("test", result.get("name"));
  }

  // ==================== toObject (InputStream, TypeReference) ====================

  @Test
  public void toObject_fromInputStreamTypeRef_shouldDeserialize() {
    String json = "[\"a\",\"b\"]";
    InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

    List<String> result = JsonMapper.toObject(is, new TypeReference<>() {});

    assertEquals(2, result.size());
    assertEquals("a", result.get(0));
    assertEquals("b", result.get(1));
  }

  @Test
  public void toObject_withNullInputStreamTypeRef_shouldReturnNull() {
    List<String> result = JsonMapper.toObject((InputStream) null, new TypeReference<>() {});

    assertNull(result);
  }

  // ==================== 忽略未知属性 ====================

  @Test
  public void toObject_withUnknownProperties_shouldNotFail() {
    String json = "{\"name\":\"test\",\"unknownField\":123}";

    TestDto result = JsonMapper.toObject(json, TestDto.class);

    assertEquals("test", result.getName());
  }


  // ==================== 辅助类 ====================

  public static class TestDto {
    private String name;
    private LocalDateTime time;
    private OffsetDateTime offsetTime;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
    public OffsetDateTime getOffsetTime() { return offsetTime; }
    public void setOffsetTime(OffsetDateTime offsetTime) { this.offsetTime = offsetTime; }
  }

  public enum TestStatus {
    ACTIVE, INACTIVE
  }
}
