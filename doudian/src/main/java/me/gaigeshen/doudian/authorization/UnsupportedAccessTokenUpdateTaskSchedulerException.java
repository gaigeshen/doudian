package me.gaigeshen.doudian.authorization;

/**
 * 不支持的访问令牌更新任务调度器
 *
 * @author gaigeshen
 */
public class UnsupportedAccessTokenUpdateTaskSchedulerException extends AccessTokenUpdateTaskSchedulerException {
  public UnsupportedAccessTokenUpdateTaskSchedulerException(String message) {
    super(message);
  }
  public UnsupportedAccessTokenUpdateTaskSchedulerException(Throwable cause) {
    super(cause);
  }
  public UnsupportedAccessTokenUpdateTaskSchedulerException(String message, Throwable cause) {
    super(message, cause);
  }
}
