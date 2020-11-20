package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.*;
import me.gaigeshen.doudian.request.content.*;
import me.gaigeshen.doudian.request.content.parser.*;
import me.gaigeshen.doudian.request.result.Result;
import me.gaigeshen.doudian.request.result.parser.ResultParser;
import me.gaigeshen.doudian.request.result.parser.ResultParserException;
import me.gaigeshen.doudian.request.result.parser.ResultParserJsonImpl;
import me.gaigeshen.doudian.util.Asserts;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

/**
 * Request executor use {@link WebClient} internal, call {@link #close()} method to close this web client,
 * execute with {@link RequestContent} and returns {@link ResponseContent},
 * can also execute with {@link Content} and returns {@link Result},
 * parse the content to {@link RequestContent} use {@link ContentParser}s,
 * parse the response content to {@link Result} use {@link ResultParser}s
 *
 * @author gaigeshen
 * @see WebClient
 * @see RequestContent
 * @see ResponseContent
 * @see Content
 * @see Result
 * @see Closeable
 */
public class RequestExecutorImpl implements RequestExecutor, Closeable {

  private final WebClient webClient; // Cannot be null

  private final ContentParserManager contentParserManager; // Cannot be null

  private final ResultParserManager resultParserManager; // Cannot be null

  /**
   * Internal constructor
   *
   * @param webClient The web client for execute {@link RequestContent}
   * @param contentParserManager The content parser manager
   * @param resultParserManager The result parser manager
   */
  private RequestExecutorImpl(WebClient webClient, ContentParserManager contentParserManager, ResultParserManager resultParserManager) {
    Asserts.notNull(webClient, "webClient");
    Asserts.notNull(contentParserManager, "contentParserManager");
    Asserts.notNull(resultParserManager, "resultParserManager");
    this.webClient = webClient;
    this.contentParserManager = contentParserManager;
    this.resultParserManager = resultParserManager;
  }

  /**
   * Create request executor with {@link WebClientConfig}, {@link ContentParser}s and {@link ResultParser}s
   *
   * @param config Web client configuration for create {@link WebClient}, cannot be null
   * @param contentParsers The content parsers, cannot be null
   * @param resultParsers The result parsers, cannot be null
   * @return Request executor
   */
  public static RequestExecutorImpl create(WebClientConfig config, Collection<ContentParser> contentParsers, Collection<ResultParser> resultParsers) {
    return new RequestExecutorImpl(new WebClient(config), new ContentParserManager(contentParsers), new ResultParserManager(resultParsers));
  }

  /**
   * Create request executor with {@link WebClientConfig}, default {@link ContentParser}s and default {@link ResultParser}s
   * <br/><br/>
   * <strong>Default content parsers</strong>
   * <ol>
   *   <li>{@link ContentParserJsonImpl}</li>
   *   <li>{@link ContentParserParametersImpl}</li>
   *   <li>{@link ContentParserMultipartParametersImpl}</li>
   * </ol>
   * <strong>Default result parsers</strong>
   * <ol>
   *   <li>{@link ResultParserJsonImpl}</li>
   * </ol>
   *
   * @param config Web client configuration for create {@link WebClient}, cannot be null
   * @return Request executor
   */
  public static RequestExecutorImpl create(WebClientConfig config) {
    return new RequestExecutorImpl(new WebClient(config), ContentParserManager.createDefault(), ResultParserManager.createDefault());
  }

  /**
   * Create request executor with default {@link WebClientConfig}, see {@link WebClientConfig#getDefault()},
   * default {@link ContentParser}s and default {@link ResultParser}s, see {@link #create(WebClientConfig)}
   *
   * @return Request executor
   */
  public static RequestExecutorImpl create() {
    return new RequestExecutorImpl(new WebClient(WebClientConfig.getDefault()), ContentParserManager.createDefault(), ResultParserManager.createDefault());
  }

  @Override
  public <R extends Result> R execute(Content<R> content) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, null);
    ResponseContent responseContent = execute(requestContent);
    return parseResult(responseContent, content.getResultClass());
  }

  @Override
  public <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, accessToken);
    ResponseContent responseContent = execute(requestContent);
    return parseResult(responseContent, content.getResultClass());
  }

  @Override
  public <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, null, urlValues);
    ResponseContent responseContent = execute(requestContent);
    return parseResult(responseContent, content.getResultClass());
  }

  @Override
  public <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, accessToken, urlValues);
    ResponseContent responseContent = execute(requestContent);
    return parseResult(responseContent, content.getResultClass());
  }

  @Override
  public ResponseContent execute(RequestContent requestContent) throws RequestExecutorException {
    try {
      return webClient.execute(requestContent);
    } catch (WebClientException e) {
      throw new RequestExecutorException("Could not execute with request content " + requestContent, e);
    }
  }

  /**
   * Parse from response content to result
   *
   * @param responseContent The response content
   * @param resultClass The result class
   * @param <R> The result type
   * @return The result
   * @throws RequestExecutorException Could not parse
   */
  private <R extends Result> R parseResult(ResponseContent responseContent, Class<R> resultClass) throws RequestExecutorException {
    try {
      return resultParserManager.parse(responseContent, resultClass);
    } catch (ResultParserException e) {
      throw new RequestExecutorException("Could not parse to result from response content, result class is " + resultClass, e);
    }
  }

  /**
   * Parse to request content from content
   *
   * @param content The content
   * @param accessToken The access token can be null or blank
   * @param urlValues The url template parameter values
   * @return Request content cannot be null
   * @throws RequestExecutorException Could not parse
   */
  private RequestContent parseRequestContent(Content<?> content, String accessToken, Object... urlValues) throws RequestExecutorException {
    try {
      return contentParserManager.parse(content, ContentHelper.getValidMetadata(content), accessToken, urlValues);
    } catch (ContentParserException | ContentMetadataException e) {
      throw new RequestExecutorException("Could not parse to request content from content " + content, e);
    }
  }

  /**
   * Call this method, close web client
   *
   * @throws IOException Some exception
   */
  @Override
  public void close() throws IOException {
    webClient.close();
  }
}
