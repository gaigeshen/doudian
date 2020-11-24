package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌更新任务调度器
 *
 * @author gaigeshen
 */
public interface AccessTokenUpdateTaskScheduler {
  /**
   * 调度访问令牌更新任务
   *
   * @param task 访问令牌更新任务不能为空
   * @param executeDelaySeconds 该任务执行的延迟时间单位秒
   * @throws AccessTokenUpdateTaskSchedulerException 无法调度访问令牌更新任务
   */
  void schedule(AccessTokenUpdateTask task, long executeDelaySeconds) throws AccessTokenUpdateTaskSchedulerException;

  /**
   * 关闭此访问令牌更新任务调度器
   *
   * @throws AccessTokenUpdateTaskSchedulerException 关闭的时候出现异常
   */
  void shutdown() throws AccessTokenUpdateTaskSchedulerException;

  /**
   * 返回此访问令牌更新任务调度器是否已被关闭
   *
   * @return 是否已被关闭
   */
  boolean isShutdown();
}
