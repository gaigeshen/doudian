package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.RequestContent;
import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 * Request executor listener
 *
 * @author gaigeshen
 */
public interface RequestExecutorListener {
  /**
   * Before parsing {@link Content}, default do nothing
   *
   * @param content The content cannot be null
   * @param accessToken The access token maybe null
   * @param urlValues The url template parameter values maybe null
   * @throws RequestExecutorListenerException Throws this exception
   */
  default void beforeContentParse(Content<?> content, String accessToken, Object... urlValues)
          throws RequestExecutorListenerException {

  }

  /**
   * Before executing, default do nothing
   *
   * @param requestContent Parsed from {@link Content}, cannot be null
   * @param content The content maybe null
   * @throws RequestExecutorListenerException Throws this exception
   */
  default void beforeExecute(RequestContent requestContent, Content<?> content) throws RequestExecutorListenerException {

  }

  /**
   * After executing, default do nothing
   *
   * @param requestContent Parsed from {@link Content}
   * @param responseContent The response content
   * @param content The content maybe null
   * @param result Parsed from response content maybe null
   * @throws RequestExecutorListenerException Throws this exception
   */
  default void afterExecute(RequestContent requestContent, ResponseContent responseContent, Content<?> content, Result result)
          throws RequestExecutorListenerException {

  }
}
