package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;

import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 访问令牌更新任务创建器，专门用于创建并帮助调度访问令牌更新任务
 *
 * @author gaigeshen
 */
public class AccessTokenUpdateTaskCreator {

  private final static long EXECUTION_TIME_BEFORE_EXPIRES = 1800;

  private final AccessTokenStore accessTokenStore;

  private final ScheduledExecutorService executorService;

  /**
   * 创建访问令牌更新任务创建器
   *
   * @param accessTokenStore 访问令牌存储器不能为空
   * @param executorService 任务调度器不能为空
   */
  public AccessTokenUpdateTaskCreator(AccessTokenStore accessTokenStore, ScheduledExecutorService executorService) {
    Asserts.notNull(accessTokenStore, "accessTokenStore");
    Asserts.notNull(executorService, "executorService");
    this.accessTokenStore = accessTokenStore;
    this.executorService = executorService;
  }

  /**
   * 通过给定的访问令牌创建并调度访问令牌更新任务
   *
   * @param accessToken 如果该访问令牌为空或者无效则调用没有任何效果
   * @see AccessTokenHelper#isValid(AccessToken)
   */
  public void createAndSchedule(AccessToken accessToken) {
    if (Objects.nonNull(accessToken) && AccessTokenHelper.isValid(accessToken)) {
      new AccessTokenUpdateTaskImpl(accessToken.getShopId()).schedule(calcExecutionDelaySeconds(accessToken.getExpiresTimestamp()));
    }
  }

  /**
   * 此方法计算给定过期时间戳距离当前时间戳的延迟时间
   *
   * @param expiresTimestamp 给定过期时间戳
   * @return 延迟时间
   */
  private long calcExecutionDelaySeconds(long expiresTimestamp) {
    return expiresTimestamp - EXECUTION_TIME_BEFORE_EXPIRES - System.currentTimeMillis() / 1000;
  }

  /**
   * 访问令牌更新任务实现
   *
   * @author gaigeshen
   */
  private class AccessTokenUpdateTaskImpl extends AbstractAccessTokenUpdateTask {

    public AccessTokenUpdateTaskImpl(String shopId) {
      setAccessTokenStore(accessTokenStore);
      setShopId(shopId);
      // 在访问令牌更新成功之后继续使用新的访问令牌创建并调度新的更新任务
      setAccessTokenUpdateListener((oat, nat) -> createAndSchedule(nat));
    }

    /**
     * 此任务只会被执行一次
     *
     * @param executeDelaySeconds 任务延迟执行的时间单位秒
     */
    public void schedule(long executeDelaySeconds) {
      executorService.schedule(this, executeDelaySeconds, TimeUnit.SECONDS);
    }

    @Override
    protected AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException {
      return null;
    }
  }
}
