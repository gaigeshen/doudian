package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 抽象的访问令牌更新任务，子类只需关注获取新的访问令牌
 *
 * @author gaigeshen
 */
public abstract class AbstractAccessTokenUpdateTask implements AccessTokenUpdateTask {

  private AccessTokenStore accessTokenStore;

  private AccessTokenUpdateListener accessTokenUpdateListener;

  private String shopId;

  /**
   * 请先设置访问令牌存储器再使用此任务
   *
   * @param accessTokenStore 访问令牌存储器
   */
  @Override
  public final void setAccessTokenStore(AccessTokenStore accessTokenStore) {
    this.accessTokenStore = Asserts.notNull(accessTokenStore, "accessTokenStore");
  }

  /**
   * 监听器可以不设置
   *
   * @param accessTokenUpdateListener 访问令牌更新监听器
   */
  @Override
  public final void setAccessTokenUpdateListener(AccessTokenUpdateListener accessTokenUpdateListener) {
    this.accessTokenUpdateListener =  Asserts.notNull(accessTokenUpdateListener, "accessTokenUpdateListener");
  }

  /**
   * 请先设置店铺编号再使用此任务
   *
   * @param shopId 店铺编号
   */
  @Override
  public final void setShopId(String shopId) {
    this.shopId = Asserts.notBlank(shopId, "shopId");
  }

  @Override
  public final AccessTokenStore getAccessTokenStore() {
    return accessTokenStore;
  }

  @Override
  public final AccessTokenUpdateListener getAccessTokenUpdateListener() {
    return accessTokenUpdateListener;
  }

  @Override
  public final String getShopId() {
    return shopId;
  }

  @Override
  public final void executeUpdate() {
    if (Objects.isNull(accessTokenStore)) {
      throw new IllegalStateException("Could not execute update because 'accessTokenStore' not configured");
    }
    if (Objects.isNull(shopId)) {
      throw new IllegalStateException("Could not execute update because 'shopId' not configured");
    }
    AccessToken currentAccessToken;
    try {
      currentAccessToken = accessTokenStore.findByShopId(shopId);
    } catch (AccessTokenStoreException e) {
      handleUpdateFailed(new AccessTokenUpdateException("Could not find current access token, the shop id is"  + shopId, e)
              .setCanRetry(true));
      return;
    }
    if (Objects.isNull(currentAccessToken)) {
      handleUpdateFailed(new AccessTokenUpdateException("No such current access token, the shop id is"  + shopId));
      return;
    }
    AccessToken accessToken;
    try {
      accessToken = executeUpdate(currentAccessToken);
    } catch (AccessTokenUpdateException ex) {
      handleUpdateFailed(ex);
      return;
    }
    if (Objects.isNull(accessToken)) {
      return;
    }
    if (!AccessTokenHelper.isValid(accessToken)) {
      handleUpdateFailed(new AccessTokenUpdateException("Could not save invalid access token to store, the shop id is " + shopId)
              .setCurrentAccessToken(currentAccessToken));
      return;
    }
    try {
      accessTokenStore.save(accessToken);
    } catch (AccessTokenStoreException e) {
      handleUpdateFailed(new AccessTokenUpdateException("Could not save access token to store, the shop id is " + shopId)
              .setCurrentAccessToken(currentAccessToken)
              .setCanRetry(true));
      return;
    }
    if (Objects.nonNull(accessTokenUpdateListener)) {
      accessTokenUpdateListener.handleUpdated(currentAccessToken, accessToken);
    }
  }

  /**
   * 直接返回店铺编号，如果没有设置店铺编号则返回固定的值
   *
   * @return 返回店铺编号
   */
  @Override
  public final String getId() {
    return StringUtils.defaultString(shopId, "NO_ID_YET");
  }

  private void handleUpdateFailed(AccessTokenUpdateException ex) {
    if (Objects.nonNull(accessTokenUpdateListener)) {
      accessTokenUpdateListener.handleFailed(ex);
    }
  }

  /**
   * 此方法用于获取新的访问令牌，获取到的访问令牌必须有效
   *
   * @param currentAccessToken 当前的访问令牌不能为空
   * @return 返回的访问令牌必须有效且不能为空，如果为空则会导致当前的任务立即结束，且不会调用监听器的相关方法
   * @throws AccessTokenUpdateException 无法获取新的访问令牌
   * @see AccessTokenHelper#isValid(AccessToken)
   */
  protected abstract AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException;
}
