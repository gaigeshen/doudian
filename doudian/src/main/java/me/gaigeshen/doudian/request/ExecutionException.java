package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 *
 * @author gaigeshen
 */
public class ExecutionException extends RequestExecutorException {

  private Content<?> content;

  private Result result;

  public ExecutionException(String message) {
    super(message);
  }
  public ExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutionException setContent(Content<?> content) {
    this.content = content;
    return this;
  }

  public ExecutionException setResult(Result result) {
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
