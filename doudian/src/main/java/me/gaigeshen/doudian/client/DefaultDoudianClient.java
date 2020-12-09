package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenNotFoundException;
import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.authorization.AccessTokenStoreException;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.http.WebClientConfig;
import me.gaigeshen.doudian.request.RequestExecutionResultException;
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
 * @author gaigeshen
 */
public class DefaultDoudianClient implements DoudianClient {

  private AppConfig appConfig;

  private AccessTokenStore accessTokenStore;

  private WebClientConfig webClientConfig;

  private Collection<ContentParser> contentParsers;

  private Collection<ResultParser> resultParsers;

  private DoudianContentCreator contentCreator;

  private RequestExecutor requestExecutor;


  public void setAppConfig(AppConfig appConfig) {
    this.appConfig = appConfig;
  }

  public void setAccessTokenStore(AccessTokenStore accessTokenStore) {
    this.accessTokenStore = accessTokenStore;
  }

  public void setWebClientConfig(WebClientConfig webClientConfig) {
    this.webClientConfig = webClientConfig;
  }

  public void setContentParsers(Collection<ContentParser> contentParsers) {
    this.contentParsers = contentParsers;
  }

  public void setResultParsers(Collection<ResultParser> resultParsers) {
    this.resultParsers = resultParsers;
  }

  public void setContentCreator(DoudianContentCreator contentCreator) {
    this.contentCreator = contentCreator;
  }

  @Override
  public <D extends DoudianData> D executeForData(DoudianParams params, String shopId) throws DoudianClientException {
    DoudianContent<D> content = contentCreator.create(params, appConfig);

    AccessToken accessToken;
    try {
      accessToken = accessTokenStore.findByShopId(shopId, true);
    } catch (AccessTokenStoreException | AccessTokenNotFoundException e) {
      if (e instanceof AccessTokenNotFoundException) {
        throw new DoudianExecutionMissingAccessTokenException(e).setContent(content);
      } else {
        throw new DoudianExecutionException(e).setContent(content);
      }
    }

    try {
      return executeForData(content, accessToken.getAccessToken());
    } catch (RequestExecutorException e) {
      if (e instanceof RequestExecutionResultException) {
        throw new DoudianExecutionResultException(e).setContent(content).setResult((DoudianResult<?>) ((RequestExecutionResultException) e).getResult());
      } else {
        throw new DoudianExecutionException(e).setContent(content);
      }
    }
  }

  @Override
  public void init() throws DoudianClientException {
    if (Objects.isNull(appConfig)) {
      throw new DoudianClientException("'appConfig' cannot be null");
    }
    if (Objects.isNull(accessTokenStore)) {
      throw new DoudianClientException("'accessTokenStore' cannot be null");
    }
    if (Objects.isNull(webClientConfig)) {
      webClientConfig = WebClientConfig.getDefault();
    }
    if (Objects.isNull(contentParsers)) {
      contentParsers = Collections.singletonList(new ContentParserParametersImpl());
    }
    if (Objects.isNull(resultParsers)) {
      resultParsers = Collections.singletonList(new ResultParserJsonImpl());
    }
    if (Objects.isNull(contentCreator)) {
      contentCreator = new DefaultDoudianContentCreator();
    }
    try {
      requestExecutor = RequestExecutorImpl.create(webClientConfig, contentParsers, resultParsers);
    } catch (Exception e) {
      throw new DoudianClientException(e);
    }
  }

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
  public void close() throws IOException {
    requestExecutor.close();
  }

}
