package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;

/**
 * 抖店客户端请求执行异常
 *
 * @author gaigeshen
 */
public class DoudianExecutionException extends DoudianClientException {

  private Content<?> content;

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
  public DoudianExecutionException setContent(Content<?> content) {
    this.content = content;
    return this;
  }

  /**
   * 返回设置的请求数据内容
   *
   * @return 请求数据内容，可能为空
   */
  public Content<?> getContent() {
    return content;
  }
}
