package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.http.WebClientConfig;
import me.gaigeshen.doudian.request.result.ResultData;
import me.gaigeshen.doudian.util.TimestampUtils;
import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 抖店远程服务请求客户端，默认实现
 *
 * @author gaigeshen
 */
public class DefaultDoudianClient extends AbstractDoudianClient {

  private final DoudianContentCreator contentCreator = new InternalDoudianContentCreator();

  /**
   * 创建默认的抖店远程服务请求客户端
   *
   * @param appConfig 应用配置不能为空
   * @param accessTokenStore 访问令牌存储器不能为空
   * @param webClientConfig 请求执行器的配置不能为空
   * @throws DoudianClientException 创建失败
   */
  public DefaultDoudianClient(AppConfig appConfig,
                              AccessTokenStore accessTokenStore,
                              WebClientConfig webClientConfig) throws DoudianClientException {
    super(appConfig, accessTokenStore, webClientConfig);
  }

  /**
   * 创建默认的抖店远程服务请求客户端，使用默认的请求执行器的配置
   *
   * @param appConfig 应用配置不能为空
   * @param accessTokenStore 访问令牌存储器不能为空
   * @throws DoudianClientException 创建失败
   */
  public DefaultDoudianClient(AppConfig appConfig,
                              AccessTokenStore accessTokenStore) throws DoudianClientException {
    super(appConfig, accessTokenStore);
  }

  @Override
  protected DoudianContentCreator getContentCreator() {
    return contentCreator;
  }

  /**
   * 默认抖店远程服务请求客户端使用的，用于创建抖店请求数据内容对象
   *
   * @author gaigeshen
   */
  private class InternalDoudianContentCreator implements DoudianContentCreator {
    private final int[] asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
    private final ObjectMapper jacksonObjectMapper = new ObjectMapper();

    public InternalDoudianContentCreator() {
      configAsciiEscapes();
      configJackconObjectMapper();
    }

    private void configAsciiEscapes() {
      asciiEscapes['<'] = CharacterEscapes.ESCAPE_STANDARD;
      asciiEscapes['>'] = CharacterEscapes.ESCAPE_STANDARD;
      asciiEscapes['&'] = CharacterEscapes.ESCAPE_STANDARD;
      asciiEscapes['\''] = CharacterEscapes.ESCAPE_STANDARD;
    }

    private void configJackconObjectMapper() {
      jacksonObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
      jacksonObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
      jacksonObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
      jacksonObjectMapper.getFactory().setCharacterEscapes(new CharacterEscapes() {
        @Override
        public int[] getEscapeCodesForAscii() {
          return asciiEscapes;
        }
        @Override
        public SerializableString getEscapeSequence(int ch) {
          return null;
        }
      });
    }

    @Override
    public <D extends ResultData> DoudianContent<D> create(DoudianParams params, AppConfig appConfig) throws DoudianContentCreationException {
      Map<String, String> orderedParams = new LinkedHashMap<>();
      for (DoudianParams.Param param : params) {
        orderedParams.put(param.getName(), String.valueOf(param.getValue()));
      }
      String paramsJsonString;
      try {
        paramsJsonString = jacksonObjectMapper.writeValueAsString(orderedParams);
      } catch (JsonProcessingException e) {
        throw new DoudianContentCreationException("Could not parse to json string from params " + params, e);
      }
      String timestamp = TimestampUtils.getCurrentTimestamp();
      String version = "2";

      String unSigned = appConfig.getAppSecret() + "app_key" + appConfig.getAppKey()
              + "method" + params.getMethod() + "param_json" + paramsJsonString
              + "timestamp" + timestamp + "v" + version + appConfig.getAppSecret();
      String signed = DigestUtils.md5Hex(unSigned);

      return DoudianContent.<D>builder()
              .setAppKey(appConfig.getAppKey())
              .setMethod(params.getMethod()).setParamJson(paramsJsonString)
              .setTimestamp(timestamp).setV(version).setSign(signed)
              .build();
    }
  }
}
