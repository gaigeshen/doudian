package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.util.Asserts;
import me.gaigeshen.doudian.util.JsonUtils;
import me.gaigeshen.doudian.util.TimestampUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gaigeshen
 */
public class DefaultDoudianContentCreator implements DoudianContentCreator {

  private final ObjectMapper jacksonObjectMapper = new ObjectMapper();

  public DefaultDoudianContentCreator() {
    jacksonObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    jacksonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    jacksonObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    jacksonObjectMapper.getFactory().setCharacterEscapes(new InternalCharacterEscapes());
  }

  @Override
  public <D> DoudianContent<D> create(DoudianParams params, AppConfig appConfig) {
    Asserts.notNull(params, "params");
    Asserts.notNull(appConfig, "appConfig");

    Map<String, String> orderedParams = new LinkedHashMap<>();
    for (DoudianParams.Param param : params) {
      orderedParams.put(param.getName(), String.valueOf(param.getValue()));
    }
    String paramJson = JsonUtils.toJson(orderedParams, jacksonObjectMapper);

    String timestamp = TimestampUtils.getCurrentTimestamp();
    String version = "2";

    String digestValue = appConfig.getAppSecret()
            + "app_key" + appConfig.getAppKey()
            + "method" + params.getMethod() + "param_json" + paramJson
            + "timestamp" + timestamp + "v" + version
            + appConfig.getAppSecret();

    return DoudianContent.<D>builder()
            .setAppKey(appConfig.getAppKey())
            .setMethod(params.getMethod()).setParamJson(paramJson)
            .setTimestamp(timestamp).setV(version).setSign(DigestUtils.md5Hex(digestValue))
            .build();
  }

  /**
   * @author gaigeshen
   */
  private class InternalCharacterEscapes extends CharacterEscapes {
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
