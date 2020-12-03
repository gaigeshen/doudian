package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.*;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.ContentHelper;
import me.gaigeshen.doudian.request.content.ContentMetadataException;
import me.gaigeshen.doudian.request.content.parser.*;
import me.gaigeshen.doudian.request.result.Result;
import me.gaigeshen.doudian.request.result.parser.ResultParser;
import me.gaigeshen.doudian.request.result.parser.ResultParserException;
import me.gaigeshen.doudian.request.result.parser.ResultParserJsonImpl;
import me.gaigeshen.doudian.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Request executor use {@link WebClient} internal,
 * call {@link #close()} method to close this web client, execute
 * with {@link RequestContent} and returns {@link ResponseContent},
 * can also execute with {@link Content} and returns {@link Result},
 * parse the content to {@link RequestContent} use {@link ContentParser}s,
 * parse the response content to {@link Result} use {@link ResultParser}s
 *
 * @author gaigeshen
 */
public class RequestExecutorImpl implements RequestExecutor {

  private final static Logger LOGGER = LoggerFactory.getLogger(RequestExecutorImpl.class);

  private final WebClient webClient; // Cannot be null

  private final ContentParserManager contentParserManager; // Cannot be null

  private final ResultParserManager resultParserManager; // Cannot be null

  private final List<RequestExecutorListener> listeners; // Cannot be null

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
    this.listeners = new ArrayList<>(Collections.singleton(new LoggerRequestExecutorListener())); // Only logger listener
  }

  /**
   * Add listener
   *
   * @param listener The listener
   */
  public void addListener(RequestExecutorListener listener) {
    if (!listeners.contains(listener)) {
      listeners.add(listener);
    }
  }

  /**
   * Remove all listeners
   */
  public void clearListeners() {
    // Remove all listeners, exclude logger listener
    listeners.removeIf(listener -> !(listener instanceof LoggerRequestExecutorListener));
  }

  /**
   * Create request executor with {@link WebClientConfig}, {@link ContentParser}s and {@link ResultParser}s
   *
   * @param config Web client configuration for create {@link WebClient}, cannot be null
   * @param contentParsers The content parsers, can be null
   * @param resultParsers The result parsers, can be null
   * @return Request executor
   */
  public static RequestExecutorImpl create(WebClientConfig config, Collection<ContentParser> contentParsers, Collection<ResultParser> resultParsers) {
    return new RequestExecutorImpl(WebClient.create(config), new ContentParserManager(contentParsers), new ResultParserManager(resultParsers));
  }

  /**
   * Create request executor with {@link WebClientConfig}, default {@link ContentParser}s and default {@link ResultParser}s
   * <br>
   * <br>
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
    return new RequestExecutorImpl(WebClient.create(config), ContentParserManager.createDefault(), ResultParserManager.createDefault());
  }

  /**
   * Create request executor with default {@link WebClientConfig}, see {@link WebClientConfig#getDefault()},
   * default {@link ContentParser}s and default {@link ResultParser}s, see {@link #create(WebClientConfig)}
   *
   * @return Request executor
   */
  public static RequestExecutorImpl create() {
    return new RequestExecutorImpl(WebClient.create(), ContentParserManager.createDefault(), ResultParserManager.createDefault());
  }

  @Override
  public <R extends Result> R execute(Content<R> content) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, null);
    ResponseContent responseContent = execute(requestContent, content);
    R result = parseResult(responseContent, content.getResultClass());
    afterExecute(requestContent, responseContent, content, result);
    return result;
  }

  @Override
  public <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, accessToken);
    ResponseContent responseContent = execute(requestContent, content);
    R result = parseResult(responseContent, content.getResultClass());
    afterExecute(requestContent, responseContent, content, result);
    return result;
  }

  @Override
  public <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, null, urlValues);
    ResponseContent responseContent = execute(requestContent, content);
    R result = parseResult(responseContent, content.getResultClass());
    afterExecute(requestContent, responseContent, content, result);
    return result;
  }

  @Override
  public <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException {
    RequestContent requestContent = parseRequestContent(content, accessToken, urlValues);
    ResponseContent responseContent = execute(requestContent, content);
    R result = parseResult(responseContent, content.getResultClass());
    afterExecute(requestContent, responseContent, content, result);
    return result;
  }

  @Override
  public ResponseContent execute(RequestContent requestContent) throws RequestExecutorException {
    ResponseContent responseContent = execute(requestContent, null);
    afterExecute(requestContent, responseContent, null, null);
    return responseContent;
  }

  /**
   * Internal execute method, call {@link #beforeExecute(RequestContent, Content)} first
   *
   * @param requestContent The request content
   * @param content The content maybe null
   * @return The response content
   * @throws RequestExecutorException Could not execute
   * @throws RequestExecutorListenerException The listener exception
   */
  private ResponseContent execute(RequestContent requestContent, Content<?>  content) throws RequestExecutorException {
    beforeExecute(requestContent, content);
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
    beforeContentParse(content, accessToken, urlValues);
    try {
      return contentParserManager.parse(content, ContentHelper.getValidMetadata(content), accessToken, urlValues);
    } catch (ContentParserException | ContentMetadataException e) {
      throw new RequestExecutorException("Could not parse to request content from content " + content, e);
    }
  }

  /**
   * Listener method
   *
   * @param content The content cannot be null
   * @param accessToken The access token maybe null
   * @param urlValues The url template parameter values maybe null
   */
  private void beforeContentParse(Content<?> content, String accessToken, Object... urlValues) {
    try {
      for (RequestExecutorListener listener : listeners) {
        listener.beforeContentParse(content, accessToken, urlValues);
      }
    } catch (RequestExecutorListenerException e) {
      LOGGER.warn("Could not call listener method 'beforeContentParse'", e);
    }
  }

  /**
   * Listener method
   *
   * @param requestContent The request content parsed from {@link Content}
   * @param content The content maybe null
   */
  private void beforeExecute(RequestContent requestContent, Content<?> content) {
    try {
      for (RequestExecutorListener listener : listeners) {
        listener.beforeExecute(requestContent, content);
      }
    } catch (RequestExecutorListenerException e) {
      LOGGER.warn("Could not call listener method 'beforeExecute'", e);
    }
  }

  /**
   * Listener method
   *
   * @param requestContent The request content cannot be null, parsed from {@link Content}
   * @param responseContent The response content cannot be null
   * @param content The content maybe null
   * @param result The result maybe null
   */
  private void afterExecute(RequestContent requestContent, ResponseContent responseContent, Content<?> content, Result result) {
    try {
      for (RequestExecutorListener listener : listeners) {
        listener.afterExecute(requestContent, responseContent, content, result);
      }
    } catch (RequestExecutorListenerException e) {
      LOGGER.warn("Could not call listener method 'afterExecute'", e);
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

  /**
   * Request executor listener just print by logger, for internal use only
   *
   * @author gaigeshen
   */
  private class LoggerRequestExecutorListener implements RequestExecutorListener {
    @Override
    public void beforeContentParse(Content<?> content, String accessToken, Object... urlValues) {
      LOGGER.info("Content: {}, Access token: {}, UrlValues: {}", content, accessToken, urlValues);
    }
    @Override
    public void beforeExecute(RequestContent requestContent, Content<?> content) {
      LOGGER.info("Request content: {}, Content: {}", requestContent, content);
    }
    @Override
    public void afterExecute(RequestContent requestContent, ResponseContent responseContent, Content<?> content, Result result) {
      LOGGER.info("Request content: {}, Response content: {}, Content: {}, Result: {}", requestContent, responseContent, content, result);
    }
  }
}
