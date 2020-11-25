package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 访问令牌管理器实现，所有的访问令牌均来自所依赖的访问令牌存储器，创建此管理器的时候，会同时为存储器中所有的访问令牌创建并调度更新任务
 *
 * @author gaigeshen
 */
public class AccessTokenManagerImpl implements AccessTokenManager {

  private static final Logger logger = LoggerFactory.getLogger(AccessTokenManagerImpl.class);

  private final AccessTokenStore accessTokenStore;

  private final AccessTokenRefresher accessTokenRefresher;

  private final AccessTokenUpdateTaskScheduler accessTokenUpdateTaskScheduler;

  /**
   * 创建访问令牌管理器，将会从访问令牌存储器中查询所有的访问令牌，并为这些访问令牌创建并调度更新任务
   *
   * @param accessTokenStore 访问令牌存储器不能为空
   * @param accessTokenRefresher 访问令牌刷新器不能为空
   * @param accessTokenUpdateTaskScheduler 访问令牌更新任务调度器不能为空，此访问令牌管理器负责该任务调度器的关闭
   * @throws AccessTokenManagerException 在为访问令牌创建并调度更新任务的时候发生异常
   */
  public AccessTokenManagerImpl(AccessTokenStore accessTokenStore,
                                AccessTokenRefresher accessTokenRefresher,
                                AccessTokenUpdateTaskScheduler accessTokenUpdateTaskScheduler) throws AccessTokenManagerException {
    Asserts.notNull(accessTokenStore, "accessTokenStore");
    Asserts.notNull(accessTokenRefresher, "accessTokenRefresher");
    Asserts.notNull(accessTokenUpdateTaskScheduler, "accessTokenUpdateTaskScheduler");

    this.accessTokenStore = accessTokenStore;
    this.accessTokenRefresher = accessTokenRefresher;
    this.accessTokenUpdateTaskScheduler = accessTokenUpdateTaskScheduler;

    createAndScheduleUpdateTasks();
  }

  /**
   * 创建访问令牌管理器，使用默认的访问令牌更新任务调度器，将会从访问令牌存储器中查询所有的访问令牌，并为这些访问令牌创建并调度更新任务
   *
   * @param accessTokenStore 访问令牌存储器不能为空
   * @param accessTokenRefresher 访问令牌刷新器不能为空
   * @throws AccessTokenManagerException 在为访问令牌创建并调度更新任务的时候发生异常
   * @see AccessTokenUpdateTaskSchedulerImpl
   */
  public AccessTokenManagerImpl(AccessTokenStore accessTokenStore,
                                AccessTokenRefresher accessTokenRefresher) throws AccessTokenManagerException {
    this(accessTokenStore, accessTokenRefresher, new AccessTokenUpdateTaskSchedulerImpl(2));
  }

  /**
   * 添加新的访问令牌，如果该访问令牌所属的店铺编号已经存在访问令牌，则会将此新的访问令牌替换掉旧的
   *
   * @param accessToken 待添加的访问令牌不能为空，可能会为此访问令牌创建并调度更新任务，这取决于该访问令牌是否为新店铺的访问令牌
   * @throws AccessTokenManagerException 无法添加访问令牌或者创建并调度更新任务的时候发生异常
   * @see #shutdown()
   */
  @Override
  public void addNewAccessToken(AccessToken accessToken) throws AccessTokenManagerException {
    Asserts.notNull(accessToken, "accessToken");
    try {
      if (!accessTokenStore.save(accessToken)) {
        return;
      }
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenManagerException("Could not add new access token " + accessToken, e);
    }
    try {
      createAndScheduleUpdateTask(accessToken);
    } catch (Exception e) {
      throw new AccessTokenManagerException("Could not schedule update task for new access token " + accessToken, e);
    }
  }

  @Override
  public void deleteAccessToken(String shopId) throws AccessTokenManagerException {
    Asserts.notNull(shopId, "shopId");
    try {
      accessTokenStore.deleteByShopId(shopId);
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenManagerException("Could not delete access token, shop id is " + shopId, e);
    }
  }

  @Override
  public AccessToken findAccessToken(String shopId) throws AccessTokenManagerException {
    Asserts.notNull(shopId, "shopId");
    try {
      return accessTokenStore.findByShopId(shopId);
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenManagerException("Could not find access token, shop id is " + shopId, e);
    }
  }

  /**
   * 内部关闭访问令牌更新任务调度器，由于添加新的访问令牌方法可能会使用到该任务调度器，所以可能会影响到该方法的正常调用，其他方法不受影响
   *
   * @throws AccessTokenManagerException 关闭的时候发生异常
   * @see #addNewAccessToken(AccessToken)
   */
  @Override
  public synchronized void shutdown() throws AccessTokenManagerException {
    try {
      accessTokenUpdateTaskScheduler.shutdown();
    } catch (AccessTokenUpdateTaskSchedulerException e) {
      throw new AccessTokenManagerException("Could not shutdown access token update task scheduler", e);
    }
  }

  /**
   * 将为所有的访问令牌创建并调度更新任务，有可能发生异常导致部分访问令牌没有成功调度更新任务，不建议继续使用此访问令牌管理器
   *
   * @throws AccessTokenManagerException 发生异常，不要继续使用此访问令牌管理器，也不要重新调用此方法
   */
  private void createAndScheduleUpdateTasks() throws AccessTokenManagerException {
    try {
      for (AccessToken accessToken : accessTokenStore.findAll()) {
        createAndScheduleUpdateTask(accessToken);
      }
    } catch (AccessTokenStoreException | AccessTokenUpdateTaskSchedulerException | InvalidAccessTokenException e) {
      throw new AccessTokenManagerException("Could not schedule update tasks", e);
    }
  }

  /**
   * 通过给定的访问令牌创建并调度访问令牌更新任务，默认的延迟执行时间为该访问令牌过期前半个小时
   *
   * @param accessToken 访问令牌不能为空，且不能为无效的访问令牌
   * @throws AccessTokenUpdateTaskSchedulerException 任务调度的时候发生异常
   * @throws InvalidAccessTokenException 无效的访问令牌
   * @see AccessTokenHelper#isValid(AccessToken)
   */
  private void createAndScheduleUpdateTask(AccessToken accessToken) throws AccessTokenUpdateTaskSchedulerException, InvalidAccessTokenException {
    long executionTimestamp = accessToken.getExpiresTimestamp() - 1800;
    createAndScheduleUpdateTask(accessToken, executionTimestamp - System.currentTimeMillis() / 1000);
  }

  /**
   * 通过给定的访问令牌创建并调度访问令牌更新任务，覆盖默认的延迟执行时间
   *
   * @param accessToken 访问令牌不能为空，且不能为无效的访问令牌
   * @param delaySeconds 延迟执行时间单位秒
   * @throws AccessTokenUpdateTaskSchedulerException 任务调度的时候发生异常
   * @throws InvalidAccessTokenException 无效的访问令牌
   * @see AccessTokenHelper#isValid(AccessToken)
   */
  private void createAndScheduleUpdateTask(AccessToken accessToken, long delaySeconds) throws AccessTokenUpdateTaskSchedulerException, InvalidAccessTokenException {
    AccessTokenUpdateTask task = createUpdateTask(accessToken, new AccessTokenUpdateListenerImpl());
    accessTokenUpdateTaskScheduler.schedule(task, delaySeconds);
  }

  /**
   * 创建访问令牌更新任务
   *
   * @param accessToken 访问令牌不能为空，且不能为无效的访问令牌
   * @param accessTokenUpdateListener 访问令牌更新监听器可以不设置
   * @return 访问令牌更新任务
   * @throws InvalidAccessTokenException 无效的访问令牌
   * @throws UnsupportedAccessTokenUpdateTaskSchedulerException 不支持的访问令牌调度器
   * @see AccessTokenHelper#isValid(AccessToken)
   */
  private AccessTokenUpdateTask createUpdateTask(AccessToken accessToken, AccessTokenUpdateListener accessTokenUpdateListener)
          throws InvalidAccessTokenException, UnsupportedAccessTokenUpdateTaskSchedulerException {
    if (!AccessTokenHelper.isValid(accessToken)) {
      throw new InvalidAccessTokenException("Could not create access token update task because access token invalid, " + accessToken);
    }
    if (accessTokenUpdateTaskScheduler instanceof AccessTokenUpdateTaskSchedulerImpl) {
      return new AccessTokenUpdateTaskImpl(accessToken.getShopId(), accessTokenUpdateListener);
    }
    if (accessTokenUpdateTaskScheduler instanceof AccessTokenUpdateTaskSchedulerQuartzImpl) {
      return new AccessTokenUpdateTaskQuartzImpl(accessToken.getShopId(), accessTokenUpdateListener);
    }
    throw new UnsupportedAccessTokenUpdateTaskSchedulerException(accessTokenUpdateTaskScheduler.getClass() + " unsupported");
  }

  /**
   * 访问令牌更新监听器实现
   *
   * @author gaigeshen
   */
  private class AccessTokenUpdateListenerImpl implements AccessTokenUpdateListener {

    @Override
    public void handleUpdated(AccessToken oldAccessToken, AccessToken newAccessToken) {
      logger.info("Access token updated, old access token is {}, new access token is {}", oldAccessToken, newAccessToken);
      try {
        createAndScheduleUpdateTask(newAccessToken);
      } catch (Exception e) {
        logger.warn("Could not schedule update task, current access token is " + newAccessToken, e);
      }
    }

    @Override
    public void handleFailed(AccessTokenUpdateException ex) {
      logger.warn("Access token update failed" + (ex.isCanRetry() ? ", retry again 10 seconds later" : "")
              + (ex.hasCurrentAccessToken() ? ", current access token is " + ex.getCurrentAccessToken() : ""), ex);
      if (ex.isCanRetry() && ex.hasCurrentAccessToken()) {
        try {
          createAndScheduleUpdateTask(ex.getCurrentAccessToken(), 10000);
        } catch (Exception e) {
          logger.warn("Could not reschedule update task, current access token is " + ex.getCurrentAccessToken(), e);
        }
      }
    }
  }

  /**
   * 访问令牌更新任务实现
   *
   * @author gaigeshen
   */
  private class AccessTokenUpdateTaskImpl extends AbstractAccessTokenUpdateTask {

    public AccessTokenUpdateTaskImpl(String shopId, AccessTokenUpdateListener accessTokenUpdateListener) {
      setAccessTokenStore(accessTokenStore);
      setShopId(shopId);
      if (Objects.nonNull(accessTokenUpdateListener)) {
        setAccessTokenUpdateListener(accessTokenUpdateListener);
      }
    }

    @Override
    protected AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException {
      return accessTokenRefresher.refresh(currentAccessToken);
    }
  }

  /**
   * 访问令牌更新任务实现，结合调度框架
   *
   * @author gaigeshen
   */
  private class AccessTokenUpdateTaskQuartzImpl extends AbstractAccessTokenUpdateTask implements AccessTokenUpdateTaskQuartzAdapter {

    public AccessTokenUpdateTaskQuartzImpl(String shopId, AccessTokenUpdateListener accessTokenUpdateListener) {
      setAccessTokenStore(accessTokenStore);
      setShopId(shopId);
      if (Objects.nonNull(accessTokenUpdateListener)) {
        setAccessTokenUpdateListener(accessTokenUpdateListener);
      }
    }

    @Override
    protected AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException {
      return accessTokenRefresher.refresh(currentAccessToken);
    }
  }
}
