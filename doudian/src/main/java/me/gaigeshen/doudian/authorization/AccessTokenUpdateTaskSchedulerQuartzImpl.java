package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌更新任务调度器实现，基于调度框架
 *
 * @author gaigeshen
 */
public class AccessTokenUpdateTaskSchedulerQuartzImpl implements AccessTokenUpdateTaskScheduler {



  @Override
  public void schedule(AccessTokenUpdateTask task, long executeDelaySeconds) throws AccessTokenUpdateTaskSchedulerException {

  }

  @Override
  public synchronized void shutdown() throws AccessTokenUpdateTaskSchedulerException {
  }

  @Override
  public boolean isShutdown() {
    return true;
  }
}
