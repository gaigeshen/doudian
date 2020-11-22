package me.gaigeshen.doudian.authorization;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

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
   * @throws JobExecutionException 任务执行失败
   */
  @Override
  default void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    try {
      executeUpdate();
    } catch (AccessTokenUpdateException e) {
      throw new JobExecutionException("Could not update access token" + (e.hasCurrentAccessToken() ? " for current access token " + e.getCurrentAccessToken() : ""), e);
    }
  }
}
