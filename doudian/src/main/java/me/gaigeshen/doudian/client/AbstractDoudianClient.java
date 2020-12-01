package me.gaigeshen.doudian.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.authorization.AccessTokenStoreImpl;
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
import me.gaigeshen.doudian.util.Asserts;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author gaigeshen
 */
public abstract class AbstractDoudianClient implements DoudianClient {

  private final AppConfig appConfig;

  private final AccessTokenStore accessTokenStore;

  private final RequestExecutor requestExecutor;

  protected AbstractDoudianClient(AppConfig appConfig) {
    this(appConfig, new AccessTokenStoreImpl());
  }

  protected AbstractDoudianClient(AppConfig appConfig, AccessTokenStore accessTokenStore) {
    this(appConfig, accessTokenStore, WebClientConfig.getDefault());
  }

  protected AbstractDoudianClient(AppConfig appConfig, AccessTokenStore accessTokenStore, WebClientConfig webClientConfig) {
    Asserts.notNull(appConfig, "appConfig");
    Asserts.notNull(accessTokenStore, "accessTokenStore");
    Asserts.notNull(webClientConfig, "webClientConfig");
    this.appConfig = appConfig;
    this.accessTokenStore = accessTokenStore;
    this.requestExecutor = RequestExecutorImpl.create(webClientConfig, getContentParsers(), getResultParsers());
  }

  protected Collection<ContentParser> getContentParsers() {
    return Collections.singletonList(new ContentParserParametersImpl());
  }

  protected Collection<ResultParser> getResultParsers() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    return Collections.singletonList(new ResultParserJsonImpl(objectMapper));
  }

  @Override
  public final AppConfig getAppConfig() {
    return appConfig;
  }

  @Override
  public final AccessTokenStore getAccessTokenStore() {
    return accessTokenStore;
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
}
