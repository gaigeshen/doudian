package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.authorization.*;
import me.gaigeshen.doudian.config.AppConfig;
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

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 抽象的抖店接口客户端，通过重写相关的配置方法可以修改默认的配置
 *
 * @author gaigeshen
 */
public abstract class AbstractDoudianClient implements DoudianClient {

  private final AppConfig appConfig;

  private final AccessTokenManager accessTokenManager;

  private final RequestExecutor requestExecutor;

  /**
   * 创建此抖店接口客户端
   *
   * @param appConfig 应用配置不能为空
   * @throws DoudianClientException 创建的时候发生异常，当前的配置不正确
   */
  protected AbstractDoudianClient(AppConfig appConfig) throws DoudianClientException {
    if (Objects.isNull(appConfig)) {
      throw new DoudianClientException("'appConfig' cannot be null");
    }
    this.appConfig = appConfig;
    this.accessTokenManager = createAccessTokenManager();
    this.requestExecutor = createRequestExecutor();
  }

  private AccessTokenManager createAccessTokenManager() throws DoudianClientException {
    AccessTokenStore accessTokenStore = getAccessTokenStore();
    if (Objects.isNull(accessTokenStore)) {
      throw new DoudianClientException("Please configure 'accessTokenStore' for doudian client");
    }
    AccessTokenExchanger accessTokenExchanger = getAccessTokenExchanger();
    if (Objects.isNull(accessTokenExchanger)) {
      throw new DoudianClientException("Please configure 'accessTokenExchanger' for doudian client");
    }
    try {
      return new AccessTokenManagerImpl(accessTokenStore, accessTokenExchanger);
    } catch (AccessTokenManagerException e) {
      throw new DoudianClientException("Could not create access token manager", e);
    }
  }

  private RequestExecutor createRequestExecutor() throws DoudianClientException {
    WebClientConfig webClientConfig = getWebClientConfig();
    if (Objects.isNull(webClientConfig)) {
      throw new DoudianClientException("Please configure 'webClientConfig' for doudian client");
    }
    Collection<ContentParser> contentParsers = getContentParsers();
    if (Objects.isNull(contentParsers)) {
      throw new DoudianClientException("Please configure 'contentParsers' for doudian client");
    }
    Collection<ResultParser> resultParsers = getResultParsers();
    if (Objects.isNull(resultParsers)) {
      throw new DoudianClientException("Please configure 'resultParsers' for doudian client");
    }
    return RequestExecutorImpl.create(webClientConfig, contentParsers, resultParsers);
  }

  protected AccessTokenStore getAccessTokenStore() {
    return new AccessTokenStoreImpl();
  }

  protected AccessTokenExchanger getAccessTokenExchanger() {
    return null;
  }

  protected WebClientConfig getWebClientConfig() {
    return WebClientConfig.getDefault();
  }

  protected Collection<ContentParser> getContentParsers() {
    return Collections.singletonList(new ContentParserParametersImpl());
  }

  protected Collection<ResultParser> getResultParsers() {
    ObjectMapper jacksonObjectMapper = new ObjectMapper();
    jacksonObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    return Collections.singletonList(new ResultParserJsonImpl(jacksonObjectMapper));
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
    try {
      requestExecutor.close();
    } finally {
      try {
        accessTokenManager.shutdown();
      } catch (AccessTokenManagerException ignored) {
      }
    }
  }
}
