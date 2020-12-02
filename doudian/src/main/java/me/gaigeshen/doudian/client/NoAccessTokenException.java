package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 * 抖店客户端请求执行异常，具体表达当前没有访问令牌造成的异常
 *
 * @author gaigeshen
 */
public class NoAccessTokenException extends ExecutionException {
  public NoAccessTokenException(String message) {
    super(message);
  }
  public NoAccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }
  public NoAccessTokenException(Throwable cause) {
    super(cause);
  }

  @Override
  public NoAccessTokenException setContent(Content<?> content) {
    super.setContent(content);
    return this;
  }

  @Override
  public ExecutionException setResult(Result result) {
    super.setResult(result);
    return this;
  }
}
