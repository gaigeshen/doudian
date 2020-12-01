package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.authorization.AccessTokenStore;
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
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * @author gaigeshen
 */
public abstract class AbstractDoudianClient implements DoudianClient {

  private AppConfig appConfig;

  private AccessTokenStore accessTokenStore;

  private WebClientConfig webClientConfig;

  private RequestExecutor requestExecutor;


  protected AppConfig getAppConfig() {
    return appConfig;
  }

  protected AccessTokenStore getAccessTokenStore() {
    return accessTokenStore;
  }

  protected ResponseContent executeInternal(RequestContent requestContent) throws ExecutionException {
    try {
      return requestExecutor.execute(requestContent);
    } catch (RequestExecutorException e) {
      throw new ExecutionException(e);
    }
  }

  protected <R extends Result> R executeInternal(Content<R> content) throws ExecutionException {
    try {
      return requestExecutor.execute(content);
    } catch (RequestExecutorException e) {
      throw new ExecutionException(e).setContent(content);
    }
  }

  protected <R extends Result> R executeInternal(Content<R> content, String accessToken) throws ExecutionException {
    try {
      return requestExecutor.execute(content, accessToken);
    } catch (RequestExecutorException e) {
      throw new ExecutionException(e).setContent(content);
    }
  }

  protected <D> D execute(DoudianContent<D> content) throws ExecutionException {
    DoudianResult<D> result = executeInternal(content);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  protected <D> D execute(DoudianContent<D> content, String accessToken) throws ExecutionException {
    DoudianResult<D> result = executeInternal(content, accessToken);
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

  private RequestExecutor createRequestExecutor() {
    return RequestExecutorImpl.create(webClientConfig, createContentParsers(), createResultParsers());
  }

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

  protected Collection<ContentParser> createContentParsers() {
    return Collections.singletonList(new ContentParserParametersImpl());
  }

  protected Collection<ResultParser> createResultParsers() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return Collections.singletonList(new ResultParserJsonImpl(objectMapper));
  }

  @Override
  public final void dispose() throws DoudianClientException {
    try {
      requestExecutor.close();
    } catch (IOException e) {
      throw new DoudianClientException(e);
    }
    disposeOthers();
  }

  protected void disposeOthers() throws DoudianClientException {

  }
}
