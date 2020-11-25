package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌更新任务调度器异常
 *
 * @author gaigeshen
 */
public class AccessTokenUpdateTaskSchedulerException extends AccessTokenException {
  public AccessTokenUpdateTaskSchedulerException(String message) {
    super(message);
  }
  public AccessTokenUpdateTaskSchedulerException(Throwable cause) {
    super(cause);
  }
  public AccessTokenUpdateTaskSchedulerException(String message, Throwable cause) {
    super(message, cause);
  }
}
