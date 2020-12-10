package me.gaigeshen.doudian.client;

/**
 * 创建抖店请求数据内容失败的时候抛出此异常
 *
 * @author gaigeshen
 */
public class DoudianContentCreationException extends DoudianExecutionException {
  public DoudianContentCreationException(String message) {
    super(message);
  }
  public DoudianContentCreationException(String message, Throwable cause) {
    super(message, cause);
  }
  public DoudianContentCreationException(Throwable cause) {
    super(cause);
  }
}
