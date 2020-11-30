package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;

/**
 * 抖店开放平台接口抽象，查看每个方法了解具体的细节
 *
 * @author gaigeshen
 */
public abstract class AbstractDoudianClient implements DoudianClient, RequestExecutor {

  private final AccessTokenStore accessTokenStore;

  private final RequestExecutor requestExecutor;

  /**
   * 此类构造采用默认的访问令牌存储器，以及默认的用于接口请求客户端的配置
   */
  protected AbstractDoudianClient() {
    this(new AccessTokenStoreImpl());
  }

  /**
   * 此类构造需要访问令牌管理器，以及默认的用于接口请求客户端的配置
   *
   * @param accessTokenStore 访问令牌存储器不能为空，仅用于查询店铺的访问令牌
   */
  protected AbstractDoudianClient(AccessTokenStore accessTokenStore) {
    this(accessTokenStore, WebClientConfig.getDefault());
  }

  /**
   * 此类构造需要访问令牌管理器和用于接口请求客户端的配置
   *
   * @param accessTokenStore 访问令牌存储器不能为空，仅用于查询店铺的访问令牌
   * @param webClientConfig 用于接口请求客户端的配置不能为空
   */
  protected AbstractDoudianClient(AccessTokenStore accessTokenStore, WebClientConfig webClientConfig) {
    this.accessTokenStore = accessTokenStore;
    this.requestExecutor = RequestExecutorImpl.create(webClientConfig, getContentParsers(), getResultParsers());
  }

  /**
   * 重写此方法覆盖默认的配置
   *
   * @return 返回请求内容转换器集合
   */
  protected Collection<ContentParser> getContentParsers() {
    return Collections.singletonList(new ContentParserParametersImpl());
  }

  /**
   * 重写此方法覆盖默认的配置
   *
   * @return 返回请求响应结果转换器集合
   */
  protected Collection<ResultParser> getResultParsers() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

    ResultParser resultParser = new ResultParserJsonImpl(objectMapper);
    return Collections.singletonList(resultParser);
  }

  /**
   * 查询访问令牌
   *
   * @param shopId 店铺编号不能为空
   * @return 访问令牌
   * @throws AccessTokenStoreException 无法查询访问令牌
   * @throws AccessTokenNotFoundException 未查询到访问令牌
   */
  protected AccessToken findAccessToken(String shopId) throws AccessTokenStoreException, AccessTokenNotFoundException {
    return accessTokenStore.findByShopId(shopId, true);
  }

  /**
   * 执行接口请求
   *
   * @param content 请求内容不能为空
   * @param <R> 请求响应结果类型
   * @return 请求响应结果不能为空
   * @throws RequestExecutorException 执行接口请求的过程发生异常
   */
  @Override
  public final <R extends Result> R execute(Content<R> content) throws RequestExecutorException {
    return requestExecutor.execute(content);
  }

  /**
   * 执行接口请求
   *
   * @param content 请求内容不能为空
   * @param accessToken 访问令牌，可以为空
   * @param <R> 请求响应结果类型
   * @return 请求响应结果不能为空
   * @throws RequestExecutorException 执行接口请求的过程发生异常
   */
  @Override
  public final <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException {
    return requestExecutor.execute(content, accessToken);
  }

  /**
   * 执行接口请求
   *
   * @param content 请求内容不能为空
   * @param urlValues 请求链接地址中的参数值
   * @param <R> 请求响应结果类型
   * @return 请求响应结果不能为空
   * @throws RequestExecutorException 执行接口请求的过程发生异常
   */
  @Override
  public final <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException {
    return requestExecutor.execute(content, urlValues);
  }

  /**
   * 执行接口请求
   *
   * @param content 请求内容不能为空
   * @param accessToken 访问令牌，可以为空
   * @param urlValues 请求链接地址中的参数值
   * @param <R> 请求响应结果类型
   * @return 请求响应结果不能为空
   * @throws RequestExecutorException 执行接口请求的过程发生异常
   */
  @Override
  public final <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException {
    return requestExecutor.execute(content, accessToken, urlValues);
  }

  /**
   * 执行接口请求，不会使用请求内容转换器，也不会使用请求响应结果转换器
   *
   * @param requestContent 请求内容不能为空
   * @return 请求响应结果不能为空
   * @throws RequestExecutorException 执行接口请求的过程发生异常
   */
  @Override
  public final ResponseContent execute(RequestContent requestContent) throws RequestExecutorException {
    return requestExecutor.execute(requestContent);
  }

  /**
   * 关闭方法，子类如果需要重写此方法，请先调用此方法
   *
   * @throws IOException 关闭发生异常
   */
  @Override
  public void close() throws IOException {
    requestExecutor.close();
  }
}
