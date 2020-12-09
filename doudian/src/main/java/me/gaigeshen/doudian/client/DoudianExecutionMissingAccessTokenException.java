package me.gaigeshen.doudian.client;

/**
 * 抖店客户端请求执行异常，具体表达当前没有访问令牌造成的异常
 *
 * @author gaigeshen
 */
public class DoudianExecutionMissingAccessTokenException extends DoudianExecutionException {
  public DoudianExecutionMissingAccessTokenException(String message) {
    super(message);
  }
  public DoudianExecutionMissingAccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }
  public DoudianExecutionMissingAccessTokenException(Throwable cause) {
    super(cause);
  }

  @Override
  public DoudianExecutionMissingAccessTokenException setContent(DoudianContent<?> content) {
    super.setContent(content);
    return this;
  }

  @Override
  public DoudianExecutionMissingAccessTokenException setResult(DoudianResult<?> result) {
    super.setResult(result);
    return this;
  }
}
