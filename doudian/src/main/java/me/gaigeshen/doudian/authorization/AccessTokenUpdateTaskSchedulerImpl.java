package me.gaigeshen.doudian.authorization;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 访问令牌更新任务调度器实现，基于可调度线程池
 *
 * @author gaigeshen
 */
public class AccessTokenUpdateTaskSchedulerImpl implements AccessTokenUpdateTaskScheduler {

  private final ScheduledExecutorService executorService;

  /**
   * 创建访问令牌更新任务调度器
   *
   * @param corePoolSize 核心线程数
   */
  public AccessTokenUpdateTaskSchedulerImpl(int corePoolSize) {
    this.executorService = Executors.newScheduledThreadPool(corePoolSize, new TaskThreadFactory());
  }

  @Override
  public void schedule(AccessTokenUpdateTask task, long executeDelaySeconds) throws AccessTokenUpdateTaskSchedulerException {
    try {
      executorService.schedule(task, executeDelaySeconds, TimeUnit.SECONDS);
    } catch (Exception e) {
      throw new AccessTokenUpdateTaskSchedulerException("Could not schedule access token update task", e);
    }
  }

  @Override
  public synchronized void shutdown() throws AccessTokenUpdateTaskSchedulerException {
    if (isShutdown()) {
      return;
    }
    executorService.shutdownNow();
    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new AccessTokenUpdateTaskSchedulerException("Shutdown access token update task scheduler failed", e);
    }
  }

  @Override
  public boolean isShutdown() {
    return executorService.isShutdown();
  }

  /**
   * 用于标记任务执行线程名称
   *
   * @author gaigeshen
   */
  private static class TaskThreadFactory implements ThreadFactory {

    private final AtomicLong suffix = new AtomicLong();

    @Override
    public Thread newThread(Runnable r) {
      return new Thread(r, "AccessTokenUpdateTask-" + suffix.incrementAndGet());
    }
  }
}
