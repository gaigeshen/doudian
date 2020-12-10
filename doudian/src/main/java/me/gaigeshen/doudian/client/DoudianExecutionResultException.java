package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 * 抖店客户端请求执行结果异常，用于表达成功执行请求之后的业务结果异常
 *
 * @author gaigeshen
 */
public class DoudianExecutionResultException extends DoudianExecutionException {

  private AbstractResult<?> result;

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
  public DoudianExecutionResultException setContent(Content<?> content) {
    super.setContent(content);
    return this;
  }

  /**
   * 设置请求执行结果
   *
   * @param result 请求执行结果
   * @return 此异常对象
   */
  public DoudianExecutionResultException setResult(AbstractResult<?> result) {
    this.result = result;
    return this;
  }

  /**
   * 返回设置的请求执行结果
   *
   * @return 请求执行结果，可能为空
   */
  public AbstractResult<?> getResult() {
    return result;
  }
}
