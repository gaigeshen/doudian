package me.gaigeshen.doudian.authorization;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * 访问令牌更新任务适配器，适配调度器框架的任务接口
 *
 * @author gaigeshen
 * @see Job
 */
public interface AccessTokenUpdateTaskQuartzAdapter extends AccessTokenUpdateTask, Job {

  /**
   * 不需要重新实现此方法，内部直接调用执行具体的更新任务方法
   *
   * @param jobExecutionContext 任务执行上下文
   */
  @Override
  default void execute(JobExecutionContext jobExecutionContext) {
    executeUpdate();
  }
}
