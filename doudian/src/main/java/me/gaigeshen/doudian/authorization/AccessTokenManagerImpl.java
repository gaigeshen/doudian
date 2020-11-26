package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 访问令牌管理器实现，所有的访问令牌均来自所依赖的访问令牌存储器，创建此管理器的时候，会同时为存储器中所有的访问令牌创建并调度更新任务
 *
 * @author gaigeshen
 */
public class AccessTokenManagerImpl implements AccessTokenManager {

  private static final Logger logger = LoggerFactory.getLogger(AccessTokenManagerImpl.class);

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

  private final AccessTokenStore accessTokenStore;

  private final AccessTokenExchanger accessTokenExchanger;

  /**
   * 创建访问令牌管理器，将会从访问令牌存储器中查询所有的访问令牌，并为这些访问令牌创建并调度更新任务
   *
   * @param accessTokenStore 访问令牌存储器不能为空
   * @param accessTokenExchanger 访问令牌换取器不能为空
   * @throws AccessTokenManagerException 在为访问令牌创建并调度更新任务的时候发生异常
   */
  public AccessTokenManagerImpl(AccessTokenStore accessTokenStore, AccessTokenExchanger accessTokenExchanger)
          throws AccessTokenManagerException {
    this.accessTokenStore = Asserts.notNull(accessTokenStore, "accessTokenStore");
    this.accessTokenExchanger = Asserts.notNull(accessTokenExchanger, "accessTokenExchanger");

    createAndScheduleUpdateTasks();
  }

  /**
   * 添加新的访问令牌，如果该访问令牌所属的店铺编号已经存在访问令牌，则会将此新的访问令牌替换掉旧的
   *
   * @param accessToken 待添加的访问令牌不能为空，可能会为此访问令牌创建并调度更新任务，这取决于该访问令牌是否为新店铺的访问令牌
   * @throws AccessTokenManagerException 无法添加访问令牌或者创建并调度更新任务的时候发生异常
   * @see #shutdown()
   */
  @Override
  public void addNewAccessToken(AccessToken accessToken) throws AccessTokenManagerException, InvalidAccessTokenException {
    Asserts.notNull(accessToken, "accessToken");
    if (!AccessTokenHelper.isValid(accessToken)) {
      throw new InvalidAccessTokenException("Could not add invalid access token " + accessToken);
    }
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
   * 关闭此访问令牌管理器，调用此方法之后，将无法创建并调度新的访问令牌更新任务，此方法的调用可能需要等待
   *
   * @throws AccessTokenManagerException 等待关闭的时候发生异常
   */
  @Override
  public synchronized void shutdown() throws AccessTokenManagerException {
    executorService.shutdownNow();
    try {
      executorService.awaitTermination(10, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new AccessTokenManagerException("Current thread interrupted while shutting down this access token manager", e);
    }
  }

  private void createAndScheduleUpdateTasks() throws AccessTokenManagerException {
    try {
      for (AccessToken accessToken : accessTokenStore.findAll()) {
        createAndScheduleUpdateTask(accessToken);
      }
    } catch (Exception e) {
      throw new AccessTokenManagerException("Could not schedule access token update tasks", e);
    }
  }

  private void createAndScheduleUpdateTask(AccessToken accessToken) {
    long executionTimestamp = accessToken.getExpiresTimestamp() - 1800;
    createAndScheduleUpdateTask(accessToken, executionTimestamp - System.currentTimeMillis() / 1000);
  }

  private void createAndScheduleUpdateTask(AccessToken accessToken, long delaySeconds) {
    executorService.schedule(createUpdateTask(accessToken), delaySeconds, TimeUnit.SECONDS);
  }

  private AccessTokenUpdateTask createUpdateTask(AccessToken accessToken) {
    return new AccessTokenUpdateTaskImpl(accessToken.getShopId());
  }

  /**
   * 访问令牌更新任务实现，使用当前访问令牌管理器关联的存储器
   *
   * @author gaigeshen
   */
  private class AccessTokenUpdateTaskImpl extends AbstractAccessTokenUpdateTask {

    public AccessTokenUpdateTaskImpl(String shopId) {
      setAccessTokenStore(accessTokenStore);
      setAccessTokenUpdateListener(new AccessTokenUpdateListenerImpl());
      setShopId(shopId);
    }

    @Override
    protected AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException {
      return accessTokenExchanger.refresh(currentAccessToken);
    }
  }

  /**
   * 访问令牌更新监听器实现，在访问令牌被更新成功之后（获取到新的访问令牌且保存到存储器中成功），将会创建并调度新的访问令牌更新任务，
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
        logger.warn("Could not schedule access token update task, current access token is " + newAccessToken, e);
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
}
