package me.gaigeshen.doudian.client;

/**
 * 抖店客户端请求执行结果异常，用于表达成功执行请求之后的业务结果异常
 *
 * @author gaigeshen
 */
public class DoudianExecutionResultException extends DoudianExecutionException {
  public DoudianExecutionResultException(String message) {
    super(message);
  }
  public DoudianExecutionResultException(String message, Throwable cause) {
    super(message, cause);
  }
  public DoudianExecutionResultException(Throwable cause) {
    super(cause);
  }

  @Override
  public DoudianExecutionResultException setContent(DoudianContent<?> content) {
    super.setContent(content);
    return this;
  }

  @Override
  public DoudianExecutionResultException setResult(DoudianResult<?> result) {
    super.setResult(result);
    return this;
  }
}
