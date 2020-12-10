package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 * After executed, the {@link AbstractResult#failed()} is true
 *
 * @author gaigeshen
 */
public class RequestExecutionResultException extends RequestExecutionException {

  private AbstractResult<?> result;

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

  public RequestExecutionResultException setResult(AbstractResult<?> result) {
    this.result = result;
    return this;
  }

  public AbstractResult<?> getResult() {
    return result;
  }
}
