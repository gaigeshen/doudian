package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 * Request executor, execute with {@link RequestContent} and returns {@link ResponseContent},
 * can also execute with {@link Content} and returns {@link Result}
 *
 * @author gaigeshen
 */
public interface RequestExecutor {
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

}
