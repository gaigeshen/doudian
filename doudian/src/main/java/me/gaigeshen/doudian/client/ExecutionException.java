package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

import java.util.Objects;

/**
 * @author gaigeshen
 */
public class ExecutionException extends DoudianClientException {

  private Content<?> content;

  private Result result;

  public ExecutionException(String message) {
    super(message);
  }

  public ExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutionException(Throwable cause) {
    super(cause);
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

  public boolean hasContent() {
    return Objects.nonNull(content);
  }

  public boolean hasResult() {
    return Objects.nonNull(result);
  }
}
