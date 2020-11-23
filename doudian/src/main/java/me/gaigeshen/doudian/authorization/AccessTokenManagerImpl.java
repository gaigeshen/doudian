package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 访问令牌管理器实现
 *
 * @author gaigeshen
 */
public class AccessTokenManagerImpl implements AccessTokenManager {

  private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);

  private AccessTokenStore accessTokenStore;

  /**
   * 检查访问令牌存储器是否已被正确设置
   *
   * @throws AccessTokenManagerException 如果访问令牌存储器为空
   */
  private void checkAccessTokenStore() throws AccessTokenManagerException {
    if (Objects.isNull(accessTokenStore)) {
      throw new AccessTokenManagerException("Please configure 'accessTokenStore' for " + this);
    }
  }

  @Override
  public void setAccessTokenStore(AccessTokenStore accessTokenStore) {
    this.accessTokenStore = Asserts.notNull(accessTokenStore, "accessTokenStore");
  }

  @Override
  public void addNewAccessToken(AccessToken accessToken) throws AccessTokenManagerException {
    checkAccessTokenStore();
    Asserts.notNull(accessToken, "accessToken");
    try {
      if (!accessTokenStore.save(accessToken)) {
        return;
      }
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenManagerException("Could not add new access token " + accessToken, e);
    }
    try {
      scheduleAccessTokenUpdateTask(accessToken);
    } catch (Exception e) {
      throw new AccessTokenManagerException("Could not schedule update task for new access token " + accessToken, e);
    }
  }

  @Override
  public void deleteAccessToken(String shopId) throws AccessTokenManagerException {
    checkAccessTokenStore();
    Asserts.notNull(shopId, "shopId");
    try {
      accessTokenStore.deleteByShopId(shopId);
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenManagerException("Could not delete access token, shop id is " + shopId, e);
    }
  }

  @Override
  public AccessToken findAccessToken(String shopId) throws AccessTokenManagerException {
    checkAccessTokenStore();
    Asserts.notNull(shopId, "shopId");
    try {
      return accessTokenStore.findByShopId(shopId);
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenManagerException("Could not find access token, shop id is " + shopId, e);
    }
  }

  /**
   * 调度访问令牌更新任务，距离该访问令牌失效还有半小时的时候执行，该更新任务执行成功的时候还会继续针对新的访问令牌调度新的任务
   *
   * @param accessToken 访问令牌
   */
  private void scheduleAccessTokenUpdateTask(AccessToken accessToken) {
    AccessTokenUpdateTask task = new AccessTokenUpdateTaskImpl();
    task.setAccessTokenStore(accessTokenStore);
    task.setAccessTokenUpdateListener((oat, nat) -> scheduleAccessTokenUpdateTask(nat));
    task.setShopId(accessToken.getShopId());
    long delay = System.currentTimeMillis() / 1000 + accessToken.getExpiresIn() - 1800;
    executorService.schedule(task, delay, TimeUnit.SECONDS);
  }

  /**
   * 访问令牌更新任务实现
   *
   * @author gaigeshen
   */
  private class AccessTokenUpdateTaskImpl extends AbstractAccessTokenUpdateTask {

    @Override
    protected AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException {
      return null;
    }
  }
}
