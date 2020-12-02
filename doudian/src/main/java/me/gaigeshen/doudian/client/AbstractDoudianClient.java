package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.http.WebClientConfig;
import me.gaigeshen.doudian.request.RequestExecutor;
import me.gaigeshen.doudian.request.RequestExecutorException;
import me.gaigeshen.doudian.request.RequestExecutorImpl;
import me.gaigeshen.doudian.request.content.AbstractContent;
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
import java.util.Objects;

/**
 * 抽象的抖店客户端，屏蔽了具体的远程服务请求细节
 *
 * @author gaigeshen
 * @see AbstractContent
 * @see DoudianContent
 * @see DoudianResult
 */
public abstract class AbstractDoudianClient implements DoudianClient {

  private AppConfig appConfig;

  private AccessTokenStore accessTokenStore;

  private WebClientConfig webClientConfig;

  private RequestExecutor requestExecutor;

  /**
   * 返回设置的应用配置
   *
   * @return 应用配置
   */
  protected AppConfig getAppConfig() {
    return appConfig;
  }

  /**
   * 返回设置的访问令牌存储器
   *
   * @return 访问令牌存储器
   */
  protected AccessTokenStore getAccessTokenStore() {
    return accessTokenStore;
  }

  /**
   * 执行远程服务请求
   *
   * @param execution 此接口的实现可以让调用者决定如何使用请求执行器
   * @param <R> 请求执行结果类型
   * @return 请求执行结果
   * @throws ExecutionException 请求执行异常
   */
  protected <R extends Result> R executeInternal(Execution<R> execution) throws ExecutionException {
    return execution.execute(new RequestExecutorWrapper());
  }

  /**
   * 执行远程服务请求
   *
   * @param content 请求数据内容
   * @param accessToken 访问令牌是可以为空的
   * @param urlValues 请求地址参数值
   * @param <R> 请求执行结果类型
   * @return 请求执行结果
   * @throws ExecutionException 请求执行异常
   */
  protected <R extends Result> R executeInternal(Content<R> content, String accessToken, Object... urlValues) throws ExecutionException {
    return executeInternal(executor -> {
      try {
        return executor.execute(content, accessToken, urlValues);
      } catch (RequestExecutorException e) {
        throw new ExecutionException(e).setContent(content);
      }
    });
  }

  /**
   * 执行远程服务请求
   *
   * @param content 请求数据内容
   * @param accessToken 访问令牌是可以为空的
   * @param <R> 请求执行结果类型
   * @return 请求执行结果
   * @throws ExecutionException 请求执行异常
   */
  protected <R extends Result> R executeInternal(Content<R> content, String accessToken) throws ExecutionException {
    return executeInternal(executor -> {
      try {
        return executor.execute(content, accessToken);
      } catch (RequestExecutorException e) {
        throw new ExecutionException(e).setContent(content);
      }
    });
  }

  /**
   * 执行远程服务请求
   *
   * @param content 请求数据内容
   * @param urlValues 请求地址参数值
   * @param <R> 请求执行结果类型
   * @return 请求执行结果
   * @throws ExecutionException 请求执行异常
   */
  protected <R extends Result> R executeInternal(Content<R> content, Object... urlValues) throws ExecutionException {
    return executeInternal(executor -> {
      try {
        return executor.execute(content, urlValues);
      } catch (RequestExecutorException e) {
        throw new ExecutionException(e).setContent(content);
      }
    });
  }

  /**
   * 执行远程服务请求
   *
   * @param content 请求数据内容
   * @param <R> 请求执行结果类型
   * @return 请求执行结果
   * @throws ExecutionException 请求执行异常
   */
  protected <R extends Result> R executeInternal(Content<R> content) throws ExecutionException {
    return executeInternal(executor -> {
      try {
        return executor.execute(content);
      } catch (RequestExecutorException e) {
        throw new ExecutionException(e).setContent(content);
      }
    });
  }

  /**
   * 执行远程服务请求，返回的请求执行结果类型为抖店请求执行结果
   *
   * @param content 请求数据内容，此数据内容通过执行请求之后将返回抖店请求执行结果
   * @param accessToken 访问令牌是可以为空的
   * @param urlValues 请求地址参数值
   * @param <D> 抖店请求执行结果中的数据部分类型
   * @return 抖店请求执行结果中的数据部分
   * @throws ExecutionException 请求执行异常
   * @throws ExecutionResultException 请求执行结果异常，可以认为请求执行业务结果是失败的
   */
  protected <D> D execute(AbstractContent<DoudianResult<D>> content, String accessToken, Object... urlValues) throws ExecutionException {
    DoudianResult<D> result = executeInternal(content, accessToken, urlValues);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  /**
   * 执行远程服务请求，返回的请求执行结果类型为抖店请求执行结果
   *
   * @param content 请求数据内容，此数据内容通过执行请求之后将返回抖店请求执行结果
   * @param accessToken 访问令牌是可以为空的
   * @param <D> 抖店请求执行结果中的数据部分类型
   * @return 抖店请求执行结果中的数据部分
   * @throws ExecutionException 请求执行异常
   * @throws ExecutionResultException 请求执行结果异常，可以认为请求执行业务结果是失败的
   */
  protected <D> D execute(AbstractContent<DoudianResult<D>> content, String accessToken) throws ExecutionException {
    DoudianResult<D> result = executeInternal(content, accessToken);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  /**
   * 执行远程服务请求，返回的请求执行结果类型为抖店请求执行结果
   *
   * @param content 请求数据内容，此数据内容通过执行请求之后将返回抖店请求执行结果
   * @param urlValues 请求地址参数值
   * @param <D> 抖店请求执行结果中的数据部分类型
   * @return 抖店请求执行结果中的数据部分
   * @throws ExecutionException 请求执行异常
   * @throws ExecutionResultException 请求执行结果异常，可以认为请求执行业务结果是失败的
   */
  protected <D> D execute(AbstractContent<DoudianResult<D>> content, Object... urlValues) throws ExecutionException {
    DoudianResult<D> result = executeInternal(content, urlValues);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  /**
   * 执行远程服务请求，返回的请求执行结果类型为抖店请求执行结果
   *
   * @param content 请求数据内容，此数据内容通过执行请求之后将返回抖店请求执行结果
   * @param <D> 抖店请求执行结果中的数据部分类型
   * @return 抖店请求执行结果中的数据部分
   * @throws ExecutionException 请求执行异常
   * @throws ExecutionResultException 请求执行结果异常，可以认为请求执行业务结果是失败的
   */
  protected <D> D execute(AbstractContent<DoudianResult<D>> content) throws ExecutionException {
    DoudianResult<D> result = executeInternal(content);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  @Override
  public final void setAppConfig(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  @Override
  public final void setAccessTokenStore(AccessTokenStore accessTokenStore) {
    this.accessTokenStore = accessTokenStore;
  }

  @Override
  public final void setWebClientConfig(WebClientConfig webClientConfig) {
    this.webClientConfig = webClientConfig;
  }

  @Override
  public final void init() throws DoudianClientException {
    checkProperties();
    requestExecutor = createRequestExecutor();
  }

  /**
   * 仅被初始化方法调用，用于创建请求执行器
   *
   * @return 创建的请求执行器
   */
  private RequestExecutor createRequestExecutor() {
    return RequestExecutorImpl.create(webClientConfig, createContentParsers(), createResultParsers());
  }

  /**
   * 检查当前所有的配置是否正确，将会被初始化方法调用，可重写改变检查策略
   *
   * @throws DoudianClientException 如果配置不正确抛出此异常
   */
  protected void checkProperties() throws DoudianClientException {
    if (Objects.isNull(appConfig)) {
      throw new DoudianClientException("The 'appConfig' must be configured");
    }
    if (Objects.isNull(accessTokenStore)) {
      throw new DoudianClientException("The 'accessTokenStore' must be configured");
    }
    if (Objects.isNull(webClientConfig)) {
      throw new DoudianClientException("The 'webClientConfig' must be configured");
    }
  }

  /**
   * 将会被初始化方法调用，返回设置的请求数据内容转换器，可重写改变默认设置
   *
   * @return 请求数据内容转换器
   */
  protected Collection<ContentParser> createContentParsers() {
    return Collections.singletonList(new ContentParserParametersImpl());
  }

  /**
   * 将会被初始化方法调用，返回设置的请求执行结果转换器，可重写改变默认设置
   *
   * @return 请求执行结果转换器
   */
  protected Collection<ResultParser> createResultParsers() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return Collections.singletonList(new ResultParserJsonImpl(objectMapper));
  }

  /**
   * 关闭方法，释放所有的在用资源，此抽象类实现将首先关闭使用的请求执行器，然后调用额外的清理释放操作方法
   *
   * @throws DoudianClientException 发生异常抛出
   */
  @Override
  public final void dispose() throws DoudianClientException {
    try {
      requestExecutor.close();
    } catch (IOException e) {
      throw new DoudianClientException(e);
    } finally {
      disposeOthers();
    }
  }

  /**
   * 如果此类需要在关闭方法被调用的时候，做额外的清理释放操作，则重写此方法，默认什么也不干
   *
   * @throws DoudianClientException 发生异常抛出
   */
  protected void disposeOthers() throws DoudianClientException {

  }

  /**
   * 用户提供此接口的实现，将传递请求执行器给用户，让用户自己决定如何使用该请求执行器并返回请求执行结果
   *
   * @author gaigeshen
   * @param <R> 请求执行结果类型
   * @see #executeInternal(Execution)
   */
  public interface Execution<R extends Result> {
    /**
     * 执行请求
     *
     * @param executor 请求执行器，实际是请求执行器包装，调用该请求执行器的关闭方法不会做任何操作
     * @return 请求执行结果
     * @throws ExecutionException 请求执行异常请抛出此异常
     * @see RequestExecutorWrapper
     */
    R execute(RequestExecutor executor) throws ExecutionException;
  }

  /**
   * 请求执行器包装，内部直接调用本类的请求执行器，为了该请求执行器避免被关闭，所以调用关闭方法不会做任何操作
   *
   * @author gaigeshen
   * @see #executeInternal(Execution)
   * @see Execution
   */
  public class RequestExecutorWrapper implements RequestExecutor {
    @Override
    public <R extends Result> R execute(Content<R> content) throws RequestExecutorException {
      return requestExecutor.execute(content);
    }
    @Override
    public <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException {
      return requestExecutor.execute(content, accessToken);
    }
    @Override
    public <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException {
      return requestExecutor.execute(content, urlValues);
    }
    @Override
    public <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException {
      return requestExecutor.execute(content, accessToken, urlValues);
    }
    @Override
    public ResponseContent execute(RequestContent requestContent) throws RequestExecutorException {
      return requestExecutor.execute(requestContent);
    }

    @Override
    public void close() {
    }
  }
}
