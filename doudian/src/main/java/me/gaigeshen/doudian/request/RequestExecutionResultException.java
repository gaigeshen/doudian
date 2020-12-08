package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 *
 * @author gaigeshen
 */
public class RequestExecutionResultException extends RequestExecutionException {
  public RequestExecutionResultException(String message) {
    super(message);
  }
  public RequestExecutionResultException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public RequestExecutionResultException setContent(Content<?> content) {
    super.setContent(content);
    return this;
  }

  @Override
  public RequestExecutionResultException setResult(Result result) {
    super.setResult(result);
    return this;
  }
}
