package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.Result;

/**
 * 抖店客户端请求执行结果异常，用于表达成功执行请求之后的业务结果异常
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
  public ExecutionResultException(Throwable cause) {
    super(cause);
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
