package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.util.Asserts;
import me.gaigeshen.doudian.util.JsonUtils;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 默认的抖店请求参数字符串序列化器
 *
 * @author gaigeshen
 */
public class DoudianParamsStringifyJsonImpl implements DoudianParamsStringify {

  private final ObjectMapper jacksonObjectMapper;

  private DoudianParamsStringifyJsonImpl(ObjectMapper jacksonObjectMapper) {
    this.jacksonObjectMapper = jacksonObjectMapper;
  }

  /**
   * 创建抖店请求参数字符串序列化器
   *
   * @param jacksonObjectMapper 序列化所使用到的对象，不能为空
   * @return 抖店请求参数字符串序列化器
   */
  public static DoudianParamsStringifyJsonImpl create(ObjectMapper jacksonObjectMapper) {
    Asserts.notNull(jacksonObjectMapper, "jacksonObjectMapper");
    return new DoudianParamsStringifyJsonImpl(jacksonObjectMapper);
  }

  /**
   * 创建抖店请求参数字符串序列化器，采用默认的配置
   *
   * @return 抖店请求参数字符串序列化器
   */
  public static DoudianParamsStringifyJsonImpl create() {
    ObjectMapper jacksonObjectMapper = new ObjectMapper();
    jacksonObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    jacksonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jacksonObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    jacksonObjectMapper.getFactory().setCharacterEscapes(new InternalCharacterEscapes());

    return create(jacksonObjectMapper);
  }

  @Override
  public String parseString(DoudianParams params, boolean paramValueStringify) {
    Map<String, Object> orderedParams = new LinkedHashMap<>();
    // 迭代出来的参数已经是按照参数名称排好序的
    for (DoudianParams.Param param : params) {
      orderedParams.put(param.getName(), paramValueStringify ? String.valueOf(param.getValue()) : param.getValue());
    }
    return JsonUtils.toJson(orderedParams, jacksonObjectMapper);
  }

  /**
   * 字符转义
   *
   * @author gaigeshen
   */
  private static class InternalCharacterEscapes extends CharacterEscapes {
    private final int[] asciiEscapes;

    private InternalCharacterEscapes() {
      int[] esc = CharacterEscapes.standardAsciiEscapesForJSON();
      esc['<'] = CharacterEscapes.ESCAPE_STANDARD;
      esc['>'] = CharacterEscapes.ESCAPE_STANDARD;
      esc['&'] = CharacterEscapes.ESCAPE_STANDARD;
      esc['\''] = CharacterEscapes.ESCAPE_STANDARD;
      asciiEscapes = esc;
    }

    @Override
    public int[] getEscapeCodesForAscii() {
      return asciiEscapes;
    }

    @Override
    public SerializableString getEscapeSequence(int ch) {
      return null;
    }
  }
}
