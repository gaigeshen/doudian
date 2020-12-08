package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 *
 * @author gaigeshen
 */
public class RequestExecutionException extends RequestExecutorException {

  private Content<?> content;

  private Result result;

  public RequestExecutionException(String message) {
    super(message);
  }
  public RequestExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestExecutionException setContent(Content<?> content) {
    this.content = content;
    return this;
  }

  public RequestExecutionException setResult(Result result) {
    this.result = result;
    return this;
  }

  public Content<?> getContent() {
    return content;
  }

  public Result getResult() {
    return result;
  }
}
