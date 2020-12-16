package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenNotFoundException;
import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.authorization.AccessTokenStoreException;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.http.WebClientConfig;
import me.gaigeshen.doudian.request.RequestExecutionException;
import me.gaigeshen.doudian.request.RequestExecutionResultException;
import me.gaigeshen.doudian.request.RequestExecutor;
import me.gaigeshen.doudian.request.RequestExecutorImpl;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.parser.ContentParser;
import me.gaigeshen.doudian.request.content.parser.ContentParserParametersImpl;
import me.gaigeshen.doudian.request.result.Result;
import me.gaigeshen.doudian.request.result.ResultData;
import me.gaigeshen.doudian.request.result.parser.ResultParser;
import me.gaigeshen.doudian.request.result.parser.ResultParserJsonImpl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 抖店远程服务请求客户端的抽象实现，子类需要继承此类来自定义相关的配置，构造函数需要的参数都是必需的
 *
 * @author gaigeshen
 */
public abstract class AbstractDoudianClient implements DoudianClient {

  private final AppConfig appConfig;

  private final AccessTokenStore accessTokenStore;

  private final RequestExecutor requestExecutor;

  /**
   * 创建抖店远程服务请求客户端
   *
   * @param appConfig 应用配置不能为空
   * @param accessTokenStore 访问令牌存储器不能为空
   * @param webClientConfig 请求执行器的配置不能为空
   * @throws DoudianClientException 创建失败
   */
  protected AbstractDoudianClient(AppConfig appConfig, AccessTokenStore accessTokenStore, WebClientConfig webClientConfig) throws DoudianClientException {
    if (Objects.isNull(appConfig)) {
      throw new DoudianClientException("'appConfig' cannot be null");
    }
    if (Objects.isNull(accessTokenStore)) {
      throw new DoudianClientException("'accessTokenStore' cannot be null");
    }
    if (Objects.isNull(webClientConfig)) {
      throw new DoudianClientException("'webClientConfig' cannot be null");
    }
    this.appConfig = appConfig;
    this.accessTokenStore = accessTokenStore;
    this.requestExecutor = createRequestExecutor(webClientConfig);
  }

  /**
   * 创建抖店远程服务请求客户端，使用默认的请求执行器的配置
   *
   * @param appConfig 应用配置不能为空
   * @param accessTokenStore 访问令牌存储器不能为空
   * @throws DoudianClientException 创建失败
   */
  protected AbstractDoudianClient(AppConfig appConfig, AccessTokenStore accessTokenStore) throws DoudianClientException {
    this(appConfig, accessTokenStore, WebClientConfig.getDefault());
  }

  /**
   * 创建请求执行器，可能会创建失败
   *
   * @param webClientConfig 请求执行器的配置
   * @return 创建成功的请求执行器
   * @throws DoudianClientException 创建失败抛出此异常
   */
  private RequestExecutor createRequestExecutor(WebClientConfig webClientConfig) throws DoudianClientException {
    try {
      return RequestExecutorImpl.create(webClientConfig, createContentParsers(), createResultParsers());
    } catch (Exception e) {
      throw new DoudianClientException("Could not create internal request executor", e);
    }
  }

  /**
   * 此方法可以重写，改变默认的请求内容转换器
   *
   * @return 返回请求内容转换器集合，可以为空表示没有配置任何转换器
   */
  protected Collection<ContentParser> createContentParsers() {
    return Collections.singletonList(new ContentParserParametersImpl());
  }

  /**
   * 此方法可以重写，改变默认的请求响应结果内容转换器
   *
   * @return 返回请求响应结果内容转换器集合，可以为空表示没有配置任何转换器
   */
  protected Collection<ResultParser> createResultParsers() {
    ObjectMapper jacksonObjectMapper = new ObjectMapper();
    jacksonObjectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    jacksonObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    jacksonObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return Collections.singletonList(new ResultParserJsonImpl(jacksonObjectMapper));
  }

  @Override
  public final AppConfig getAppConfig() {
    return appConfig;
  }

  /**
   * 返回此客户端使用的访问令牌存储器，只能调用该存储器的查询相关的方法
   *
   * @return 访问令牌存储器，不为空
   */
  @Override
  public final AccessTokenStore getAccessTokenStore() {
    return new AccessTokenStore() {
      @Override
      public boolean save(AccessToken accessToken) throws AccessTokenStoreException {
        throw new AccessTokenStoreException("Can only call 'FIND' methods of access token store");
      }
      @Override
      public void deleteByShopId(String shopId) throws AccessTokenStoreException {
        throw new AccessTokenStoreException("Can only call 'FIND' methods of access token store");
      }
      @Override
      public AccessToken findByShopId(String shopId) throws AccessTokenStoreException {
        return accessTokenStore.findByShopId(shopId);
      }
      @Override
      public List<AccessToken> findAll() throws AccessTokenStoreException {
        return accessTokenStore.findAll();
      }
    };
  }

  /**
   * 返回此客户端使用的请求执行器，调用该请求执行器的关闭方法无任何效果
   *
   * @return 请求执行器，不为空
   */
  @Override
  public final RequestExecutor getRequestExecutor() {
    return new RequestExecutor() {
      @Override
      public <R extends Result> R execute(Content<R> content) throws RequestExecutionException {
        return requestExecutor.execute(content);
      }
      @Override
      public <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutionException {
        return requestExecutor.execute(content, accessToken);
      }
      @Override
      public <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutionException {
        return requestExecutor.execute(content, urlValues);
      }
      @Override
      public <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutionException {
        return requestExecutor.execute(content, accessToken, urlValues);
      }
      @Override
      public ResponseContent execute(RequestContent requestContent) throws RequestExecutionException {
        return requestExecutor.execute(requestContent);
      }
      @Override
      public void close() throws IOException {
      }
    };
  }

  @Override
  public <D extends ResultData> D execute(DoudianParamsSource paramsSource, String method, String shopId) throws DoudianExecutionException {
    if (Objects.isNull(paramsSource) || Objects.isNull(shopId)) {
      throw new DoudianExecutionException("'paramsSource' and 'shopId' cannot be null");
    }
    if (Objects.isNull(method)) {
      throw new DoudianExecutionException("'method' cannot be null");
    }
    DoudianParams params = DoudianParamsBuilderFromSourceImpl.getInstance().build(method, paramsSource);
    return execute(params, shopId);
  }

  @Override
  public final <D extends ResultData> D execute(DoudianParams params, String shopId) throws DoudianExecutionException {
    DoudianContentCreator contentCreator = getContentCreator();
    if (Objects.isNull(contentCreator)) {
      throw new DoudianExecutionException("No content creator for create content from params " + params);
    }
    if (Objects.isNull(params) || Objects.isNull(shopId)) {
      throw new DoudianExecutionException("'params' and 'shopId' cannot be null");
    }
    DoudianContent<D> content = contentCreator.create(params, appConfig);
    AccessToken accessToken = findAccessToken(shopId, content);

    return execute(executor -> {
      try {
        return executor.executeForData(content, accessToken.getAccessToken());
      } catch (RequestExecutionException e) {
        if (e instanceof RequestExecutionResultException) {
          throw new DoudianExecutionResultException(e).setContent(e.getContent()).setResult(((RequestExecutionResultException) e).getResult());
        }
        throw new DoudianExecutionException(e).setContent(e.getContent());
      }
    });
  }

  /**
   * 返回指定店铺的访问令牌，如果不存在该店铺的访问令牌则抛出异常，该访问令牌用于执行抖店远程服务请求
   *
   * @param shopId 店铺编号不能为空
   * @param content 执行远程服务请求的时候采用的抖店请求数据内容
   * @return 访问令牌不为空
   * @throws DoudianExecutionException 访问令牌存储器异常会被转成此异常
   * @throws DoudianExecutionMissingAccessTokenException 该店铺不存在访问令牌
   */
  private AccessToken findAccessToken(String shopId, DoudianContent<?> content) throws DoudianExecutionException {
    try {
      return accessTokenStore.findByShopId(shopId, true);
    } catch (AccessTokenStoreException | AccessTokenNotFoundException e) {
      if (e instanceof AccessTokenNotFoundException) {
        throw new DoudianExecutionMissingAccessTokenException(e).setContent(content);
      } else {
        throw new DoudianExecutionException(e).setContent(content);
      }
    }
  }

  @Override
  public final void close() throws IOException {
    requestExecutor.close();
  }

  /**
   * 设置抖店请求数据内容创建器，此方法需要子类实现，来规定如何将抖店请求参数转换为抖店请求数据内容，将在执行远程服务请求的时候被调用
   *
   * @return 返回抖店请求数据内容创建器，不能为空，如果为空则执行远程服务请求的时候抛出抖店客户端请求执行异常
   */
  protected abstract DoudianContentCreator getContentCreator();
}
