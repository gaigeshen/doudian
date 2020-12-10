package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.request.content.Content;

/**
 * Could not execute, this exception object maybe include {@link Content} object
 *
 * @author gaigeshen
 */
public class RequestExecutionException extends RequestExecutorException {

  private Content<?> content;

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

  public Content<?> getContent() {
    return content;
  }
}
