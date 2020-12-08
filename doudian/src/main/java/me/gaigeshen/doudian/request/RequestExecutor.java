package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.AbstractResult;
import me.gaigeshen.doudian.request.result.Result;

import java.io.Closeable;
import java.io.IOException;

/**
 * Request executor, execute with {@link RequestContent} and returns {@link ResponseContent},
 * can also execute with {@link Content} and returns {@link Result}
 *
 * @author gaigeshen
 */
public interface RequestExecutor extends Closeable {
  /**
   * Execute with content and returns result
   *
   * @param content The content cannot be null
   * @param <R> The result type
   * @return The result
   * @throws RequestExecutorException Could not execute
   */
  <R extends Result> R execute(Content<R> content) throws RequestExecutorException;

  /**
   * Execute with content and returns result
   *
   * @param content The content cannot be null
   * @param accessToken The access token, can be null or blank
   * @param <R> The result type
   * @return The result
   * @throws RequestExecutorException Could not execute
   */
  <R extends Result> R execute(Content<R> content, String accessToken) throws RequestExecutorException;

  /**
   * Execute with content and returns result
   *
   * @param content The content cannot be null
   * @param urlValues The url template parameter values
   * @param <R> The result type
   * @return The result
   * @throws RequestExecutorException Could not execute
   */
  <R extends Result> R execute(Content<R> content, Object... urlValues) throws RequestExecutorException;

  /**
   * Execute with content and returns result
   *
   * @param content The content cannot be null
   * @param accessToken The access token, can be null or blank
   * @param urlValues The url template parameter values
   * @param <R> The result type
   * @return The result
   * @throws RequestExecutorException Could not execute
   */
  <R extends Result> R execute(Content<R> content, String accessToken, Object... urlValues) throws RequestExecutorException;

  /**
   * Execute with request content and returns response content
   *
   * @param requestContent The request content cannot be null
   * @return The response content
   * @throws RequestExecutorException Could not execute
   */
  ResponseContent execute(RequestContent requestContent) throws RequestExecutorException;

  /**
   * Execute with abstract content and returns abstract result include data
   *
   * @param content The abstract content cannot be null
   * @param <D> The result data type
   * @return The result data
   * @throws RequestExecutorException Could not execute
   * @throws ExecutionResultException If the abstract result is failed
   */
  default <D> D executeForData(AbstractContent<? extends AbstractResult<D>> content) throws RequestExecutorException {
    AbstractResult<D> result = execute(content);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  /**
   * Execute with abstract content and returns abstract result include data
   *
   * @param content The abstract content cannot be null
   * @param accessToken The access token, can be null or blank
   * @param <D> The result data type
   * @return The result data
   * @throws RequestExecutorException Could not execute
   * @throws ExecutionResultException If the abstract result is failed
   */
  default <D> D executeForData(AbstractContent<? extends AbstractResult<D>> content, String accessToken) throws RequestExecutorException {
    AbstractResult<D> result = execute(content, accessToken);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  /**
   * Execute with abstract content and returns abstract result include data
   *
   * @param content The abstract content cannot be null
   * @param urlValues The url template parameter values
   * @param <D> The result data type
   * @return The result data
   * @throws RequestExecutorException Could not execute
   * @throws ExecutionResultException If the abstract result is failed
   */
  default <D> D executeForData(AbstractContent<? extends AbstractResult<D>> content, Object... urlValues) throws RequestExecutorException {
    AbstractResult<D> result = execute(content, urlValues);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  /**
   * Execute with abstract content and returns abstract result include data
   *
   * @param content The abstract content cannot be null
   * @param accessToken The access token, can be null or blank
   * @param urlValues The url template parameter values
   * @param <D> The result data type
   * @return The result data
   * @throws RequestExecutorException Could not execute
   * @throws ExecutionResultException If the abstract result is failed
   */
  default <D> D executeForData(AbstractContent<? extends AbstractResult<D>> content, String accessToken, Object... urlValues) throws RequestExecutorException {
    AbstractResult<D> result = execute(content, accessToken, urlValues);
    if (result.failed()) {
      throw new ExecutionResultException(result.getMessage()).setContent(content).setResult(result);
    }
    return result.getData();
  }

  /**
   * Close this request executor
   *
   * @throws IOException Could not be closed
   */
  @Override
  void close() throws IOException;
}
