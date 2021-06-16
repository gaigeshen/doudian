package me.gaigeshen.doudian.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Json utils, use jackson lib
 *
 * @author gaigeshen
 */
public class JsonUtils {
  /**
   * Default object mapper
   */
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private JsonUtils() { }

  /**
   * Parse to json string, use default jackson object mapper
   *
   * @param object The object to be parse to json string
   * @return Json string parsed
   */
  public static String toJson(Object object) {
    return toJson(object, objectMapper);
  }

  /**
   * Parse to json string
   *
   * @param object The object to be parse to json string
   * @param objectMapper Use this jackson object mapper
   * @return Json string parsed
   */
  public static String toJson(Object object, ObjectMapper objectMapper) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Cannot parse to json from: " + object);
    }
  }

  /**
   * Parse to object from json string, use default jackson object mapper
   *
   * @param json Json string
   * @param targetClass Target object class
   * @param <T> Target object type
   * @return Parsed object
   */
  public static <T> T parseObject(String json, Class<T> targetClass) {
    try {
      return objectMapper.readValue(json, targetClass);
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot parse to object from: " + json);
    }
  }

  /**
   * Parse to object from json string
   *
   * @param json Json string
   * @param objectMapper Use this jackson object mapper
   * @param targetClass Target object class
   * @param <T> Target object type
   * @return Parsed object
   */
  public static <T> T parseObject(String json, ObjectMapper objectMapper, Class<T> targetClass) {
    try {
      return objectMapper.readValue(json, targetClass);
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot parse to object from: " + json);
    }
  }

  /**
   * Parse to array from json string, use default jackson object mapper
   *
   * @param json Json string
   * @param <T> Target object class of array item
   * @return Parsed array list
   */
  public static <T> List<T> parseArray(String json) {
    try {
      return objectMapper.readValue(json, new TypeReference<List<T>>() {});
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot parse to array from: " + json);
    }
  }

  /**
   * Parse to array from json string
   *
   * @param json Json string
   * @param objectMapper Use this jackson object mapper
   * @param <T> Target object class of array item
   * @return Parsed array list
   */
  public static <T> List<T> parseArray(String json, ObjectMapper objectMapper) {
    try {
      return objectMapper.readValue(json, new TypeReference<List<T>>() {});
    } catch (IOException e) {
      throw new IllegalArgumentException("Cannot parse to array from: " + json);
    }
  }

  /**
   * Parse to map from json string, use default jackson object mapper
   *
   * @param json Json string
   * @return Parsed map
   */
  public static Map<String, Object> parseMapping(String json) {
    return parseMapping(parseJsonNode(json));
  }

  /**
   * Parse to map from json node
   *
   * @param jsonNode Json node
   * @return Parsed map
   */
  public static Map<String, Object> parseMapping(JsonNode jsonNode) {
    if (!jsonNode.isObject()) {
      throw new IllegalArgumentException("Can only support json object, but json input: " + jsonNode);
    }
    Map<String, Object> result = new HashMap<>();
    Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
    while (fields.hasNext()) {
      Map.Entry<String, JsonNode> field = fields.next();
      JsonNode value = field.getValue();
      if (value.isArray()) {
        List<Map<String, Object>> array = new LinkedList<>();
        for (JsonNode internalJsonNode : value) {
          array.add(parseMapping(internalJsonNode));
        }
        result.put(field.getKey(), array);
        continue;
      }
      if (value.isValueNode()) {
        if (value.isBoolean()) {
          result.put(field.getKey(), value.booleanValue());
        } else if (value.isTextual()) {
          result.put(field.getKey(), value.textValue());
        } else if (value.isFloat()) {
          result.put(field.getKey(), value.floatValue());
        } else if (value.isDouble()) {
          result.put(field.getKey(), value.doubleValue());
        } else if (value.isInt()) {
          result.put(field.getKey(), value.intValue());
        } else if (value.isLong()) {
          result.put(field.getKey(), value.longValue());
        } else if (value.isNull()) {
          result.put(field.getKey(), null);
        }
      }
    }
    return result;
  }

  /**
   * Parse to json node from json string, use default jackson object mapper
   *
   * @param json Json string
   * @return Parsed json node
   */
  public static JsonNode parseJsonNode(String json) {
    JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(json);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid json input: " + json);
    }
    return jsonNode;
  }

  /**
   * Parse to json node from json string, use default jackson object mapper
   *
   * @param json Json string
   * @param field Set field filter
   * @return Parsed json node
   */
  public static JsonNode parseJsonNode(String json, String field) {
    JsonNode jsonNode;
    try {
      jsonNode = objectMapper.readTree(json);
    } catch (IOException e) {
      throw new IllegalArgumentException("Invalid json input: " + json);
    }
    if (Objects.isNull(jsonNode) || !jsonNode.isObject()) {
      throw new IllegalArgumentException("Can only support json object, but input json is: " + json);
    }
    if (StringUtils.isNotBlank(field)) {
      jsonNode = jsonNode.get(field);
    }
    if (Objects.isNull(jsonNode)) {
      throw new IllegalArgumentException("Missing field, but input json is: " + json);
    }
    return jsonNode;
  }

  /**
   * Parse to json node from json node
   *
   * @param jsonNode Json node
   * @param field Set field filter
   * @return Parsed json node
   */
  public static JsonNode parseJsonNode(JsonNode jsonNode, String field) {
    JsonNode fieldJsonNode = jsonNode.get(field);
    if (Objects.isNull(fieldJsonNode)) {
      throw new IllegalArgumentException("Missing field, but input json is: " + jsonNode);
    }
    return fieldJsonNode;
  }

  /**
   * Parse to boolean value from json node
   *
   * @param jsonNode Json node
   * @return Boolean value
   */
  public static boolean parseBooleanValue(JsonNode jsonNode) {
    if (!jsonNode.isBoolean()) {
      throw new IllegalArgumentException("Cannot parse to boolean from: " + jsonNode);
    }
    return jsonNode.booleanValue();
  }

  /**
   * Parse to string value from json node
   *
   * @param jsonNode Json node
   * @return String value
   */
  public static String parseStringValue(JsonNode jsonNode) {
    if (!jsonNode.isTextual()) {
      throw new IllegalArgumentException("Cannot parse to string from: " + jsonNode);
    }
    return jsonNode.textValue();
  }

  /**
   * Parse to int value from json node
   *
   * @param jsonNode Json node
   * @return Int value
   */
  public static int parseIntValue(JsonNode jsonNode) {
    if (!jsonNode.canConvertToInt()) {
      throw new IllegalArgumentException("Cannot parse to int from: " + jsonNode);
    }
    return jsonNode.asInt();
  }

  /**
   * Parse to long value from json node
   *
   * @param jsonNode Json node
   * @return Long value
   */
  public static long parseLongValue(JsonNode jsonNode) {
    if (!jsonNode.canConvertToLong()) {
      throw new IllegalArgumentException("Cannot parse to long from: " + jsonNode);
    }
    return jsonNode.asLong();
  }

  /**
   * Parse to boolean value from json string, use default jackson object
   *
   * @param json Json string
   * @return Boolean value
   */
  public static boolean parseBooleanValue(String json) {
    return parseBooleanValue(parseJsonNode(json));
  }

  /**
   * Parse to string value from json string, use default jackson object
   *
   * @param json Json string
   * @return String value
   */
  public static String parseStringValue(String json) {
    return parseStringValue(parseJsonNode(json));
  }

  /**
   * Parse to int value from json string, use default jackson object
   *
   * @param json Json string
   * @return Int value
   */
  public static int parseIntValue(String json) {
    return parseIntValue(parseJsonNode(json));
  }

  /**
   * Parse to long value from json string, use default jackson object
   *
   * @param json Json string
   * @return Long value
   */
  public static long parseLongValue(String json) {
    return parseLongValue(parseJsonNode(json));
  }

  /**
   * Parse to boolean value from json string, use default jackson object
   *
   * @param json Json string
   * @param field Set field filter
   * @return Boolean value
   */
  public static boolean parseBooleanValue(String json, String field) {
    return parseBooleanValue(parseJsonNode(json, field));
  }

  /**
   * Parse to string value from json string, use default jackson object
   *
   * @param json Json string
   * @param field Set field filter
   * @return String value
   */
  public static String parseStringValue(String json, String field) {
    return parseStringValue(parseJsonNode(json, field));
  }

  /**
   * Parse to int value from json string, use default jackson object
   *
   * @param json Json string
   * @param field Set field filter
   * @return Int value
   */
  public static int parseIntValue(String json, String field) {
    return parseIntValue(parseJsonNode(json, field));
  }

  /**
   * Parse to long value from json string, use default jackson object
   *
   * @param json Json string
   * @param field Set field filter
   * @return Long value
   */
  public static long parseLongValue(String json, String field) {
    return parseLongValue(parseJsonNode(json, field));
  }

  /**
   * Parse to boolean value from json node
   *
   * @param jsonNode Json node
   * @param field Set field filter
   * @return Boolean value
   */
  public static boolean parseBooleanValue(JsonNode jsonNode, String field) {
    return parseBooleanValue(parseJsonNode(jsonNode, field));
  }

  /**
   * Parse to string value from json node
   *
   * @param jsonNode Json node
   * @param field Set field filter
   * @return String value
   */
  public static String parseStringValue(JsonNode jsonNode, String field) {
    return parseStringValue(parseJsonNode(jsonNode, field));
  }

  /**
   * Parse to int value from json node
   *
   * @param jsonNode Json node
   * @param field Set field filter
   * @return Int value
   */
  public static int parseIntValue(JsonNode jsonNode, String field) {
    return parseIntValue(parseJsonNode(jsonNode, field));
  }

  /**
   * Parse to long value from json node
   *
   * @param jsonNode Json node
   * @param field Set field filter
   * @return Long value
   */
  public static long parseLongValue(JsonNode jsonNode, String field) {
    return parseLongValue(parseJsonNode(jsonNode, field));
  }
}
