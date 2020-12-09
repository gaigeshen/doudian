package me.gaigeshen.doudian.client;

/**
 * 抖店客户端请求执行异常
 *
 * @author gaigeshen
 */
public class DoudianExecutionException extends DoudianClientException {

  private DoudianContent<?> content;

  private DoudianResult<?> result;

  public DoudianExecutionException(String message) {
    super(message);
  }
  public DoudianExecutionException(String message, Throwable cause) {
    super(message, cause);
  }
  public DoudianExecutionException(Throwable cause) {
    super(cause);
  }

  /**
   * 设置请求数据内容
   *
   * @param content 请求数据内容
   * @return 此异常对象
   */
  public DoudianExecutionException setContent(DoudianContent<?> content) {
    this.content = content;
    return this;
  }

  /**
   * 设置请求执行结果
   *
   * @param result 请求执行结果
   * @return 此异常对象
   */
  public DoudianExecutionException setResult(DoudianResult<?> result) {
    this.result = result;
    return this;
  }

  /**
   * 返回设置的请求数据内容
   *
   * @return 请求数据内容，可能为空
   */
  public DoudianContent<?> getContent() {
    return content;
  }

  /**
   * 返回设置的请求执行结果
   *
   * @return 请求执行结果，可能为空
   */
  public DoudianResult<?> getResult() {
    return result;
  }
}
