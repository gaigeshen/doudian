package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 *
 * @author gaigeshen
 */
public class ExecutionResultException extends ExecutionException {
  public ExecutionResultException(String message) {
    super(message);
  }
  public ExecutionResultException(String message, Throwable cause) {
    super(message, cause);
  }

  @Override
  public ExecutionResultException setContent(Content<?> content) {
    super.setContent(content);
    return this;
  }

  @Override
  public ExecutionResultException setResult(Result result) {
    super.setResult(result);
    return this;
  }
}
