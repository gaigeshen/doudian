package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.gaigeshen.doudian.authorization.*;
import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.http.WebClientConfig;
import me.gaigeshen.doudian.request.RequestExecutor;
import me.gaigeshen.doudian.request.RequestExecutorException;
import me.gaigeshen.doudian.request.RequestExecutorImpl;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.parser.ContentParser;
import me.gaigeshen.doudian.request.content.parser.ContentParserParametersImpl;
import me.gaigeshen.doudian.request.result.Result;
import me.gaigeshen.doudian.request.result.parser.ResultParser;
import me.gaigeshen.doudian.request.result.parser.ResultParserJsonImpl;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.PropertyNamingStrategy.SNAKE_CASE;

/**
 * 抽象的抖店接口客户端，通过重写相关的配置方法可以修改默认的配置，在创建的时候初始化内部的访问令牌管理器和请求执行器
 *
 * @author gaigeshen
 */
public abstract class AbstractDoudianClient implements DoudianClient {

  private final AccessTokenManager accessTokenManager;

  private final RequestExecutor requestExecutor;

  protected AbstractDoudianClient() {
    this.accessTokenManager = createAccessTokenManager();
    this.requestExecutor = createRequestExecutor();
  }

  private AccessTokenManager createAccessTokenManager() {
    AccessTokenStore accessTokenStore = getAccessTokenStore();
    if (Objects.isNull(accessTokenStore)) {
      throw new DoudianClientConfigException("'accessTokenStore' cannot be null");
    }
    try {
      return new AccessTokenManagerImpl(accessTokenStore, new AccessTokenRefresher() {
        @Override
        public AccessToken refresh(AccessToken oldAccessToken) throws AccessTokenRefreshException {
          return refreshAccessToken(oldAccessToken);
        }
      });
    } catch (AccessTokenManagerException e) {
      throw new DoudianClientException("Could not create access token manager", e);
    }
  }

  private RequestExecutor createRequestExecutor() {
    WebClientConfig webClientConfig = getWebClientConfig();
    if (Objects.isNull(webClientConfig)) {
      throw new DoudianClientConfigException("'webClientConfig' cannot be null");
    }
    Collection<ContentParser> contentParsers = getContentParsers();
    if (Objects.isNull(contentParsers)) {
      throw new DoudianClientConfigException("'contentParsers' cannot be null");
    }
    Collection<ResultParser> resultParsers = getResultParsers();
    if (Objects.isNull(resultParsers)) {
      throw new DoudianClientConfigException("'resultParsers' cannot be null");
    }
    return RequestExecutorImpl.create(webClientConfig, contentParsers, resultParsers);
  }

  /**
   * 重写此方法可以返回指定的访问令牌存储器，默认是简单的基于内存的哈希存储，此类存储器不适合在生产环境使用，在系统重启时会丢失所有的访问令牌
   *
   * @return 访问令牌存储器
   */
  protected AccessTokenStore getAccessTokenStore() {
    return new AccessTokenStoreImpl();
  }

  /**
   * 重写此方法可以返回指定的配置用于网络请求，有默认配置
   *
   * @return 配置用于网络请求
   */
  protected WebClientConfig getWebClientConfig() {
    return WebClientConfig.getDefault();
  }

  /**
   * 重写此方法可以返回指定的请求内容转换器集合，有默认配置
   *
   * @return 请求内容转换器集合
   */
  protected Collection<ContentParser> getContentParsers() {
    return Collections.singletonList(new ContentParserParametersImpl());
  }

  /**
   * 重写此方法可以返回指定的响应内容转换器集合，有默认配置
   *
   * @return 响应内容转换器集合
   */
  protected Collection<ResultParser> getResultParsers() {
    ObjectMapper jacksonObjectMapper = new ObjectMapper();
    jacksonObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    jacksonObjectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    jacksonObjectMapper.setPropertyNamingStrategy(SNAKE_CASE);
    return Collections.singletonList(new ResultParserJsonImpl(jacksonObjectMapper));
  }

  @Override
  public final void processAuthorizationCode(String authorizationCode) throws AuthorizationException {
    if (StringUtils.isBlank(authorizationCode)) {
      throw new AuthorizationException("Could not process blank or null authorization code");
    }
    AccessToken accessToken = getAccessToken(authorizationCode);
    if (Objects.nonNull(accessToken)) {
      try {
        addNewAccessToken(accessToken);
      } catch (AccessTokenManagerException | InvalidAccessTokenException e) {
        throw new AuthorizationException("Could not process authorization code " + authorizationCode, e);
      }
    }
  }

  @Override
  public final void addNewAccessToken(AccessToken accessToken) throws AccessTokenManagerException, InvalidAccessTokenException {
    accessTokenManager.addNewAccessToken(accessToken);
  }

  @Override
  public final void deleteAccessToken(String shopId) throws AccessTokenManagerException {
    accessTokenManager.deleteAccessToken(shopId);
  }

  @Override
  public final AccessToken findAccessToken(String shopId) throws AccessTokenManagerException {
    return accessTokenManager.findAccessToken(shopId);
  }

  @Override
  public final void shutdown() throws AccessTokenManagerException {
    accessTokenManager.shutdown();
  }

  @Override
  public final <R extends Result> R execute(Content<R> content) throws RequestExecutorException {
    return requestExecutor.execute(content);
  }

  @Override
  public final <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException {
    return requestExecutor.execute(content, accessToken);
  }

  @Override
  public final <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException {
    return requestExecutor.execute(content, urlValues);
  }

  @Override
  public final <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException {
    return requestExecutor.execute(content, accessToken, urlValues);
  }

  @Override
  public final ResponseContent execute(RequestContent requestContent) throws RequestExecutorException {
    return requestExecutor.execute(requestContent);
  }

  @Override
  public final void close() throws IOException {
    requestExecutor.close();
  }

  @Override
  public final void shutdownAndClose() {
    try {
      shutdown();
    } catch (AccessTokenManagerException e) {
      // 关闭访问令牌管理器的功能发生异常
    }
    try {
      close();
    } catch (IOException e) {
      // 关闭请求执行器的时候发生异常
    }
  }

  /**
   * 实现此方法用于通过授权码获取访问令牌
   *
   * @param authorizationCode 授权码不能为空
   * @return 访问令牌不能为空
   * @throws AuthorizationException 处理授权码过程发生异常
   */
  protected abstract AccessToken getAccessToken(String authorizationCode) throws AuthorizationException;

  /**
   * 实现此方法用于刷新访问令牌
   *
   * @param oldAccessToken 旧的访问令牌不能为空
   * @return 新的访问令牌不能为空
   * @throws AccessTokenRefreshException 刷新访问令牌异常
   */
  protected abstract AccessToken refreshAccessToken(AccessToken oldAccessToken) throws AccessTokenRefreshException;
}
