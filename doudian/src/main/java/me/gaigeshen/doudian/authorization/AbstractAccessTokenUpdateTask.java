package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;

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

  /**
   * 此方法内部首先从存储器中获取当前的访问令牌，然后将该访问令牌交由子类去获取新的访问令牌，获取到的新的访问令牌可以为空，
   * 表示不进行任何更新操作，如果不为空，则最后保存该新的访问令牌到存储器中
   *
   * @throws AccessTokenUpdateException 更新过程中发生异常
   */
  @Override
  public final void executeUpdate() throws AccessTokenUpdateException {
    if (Objects.isNull(accessTokenStore)) {
      throw new AccessTokenUpdateException("Could not execute update because 'accessTokenStore' not configured");
    }
    if (Objects.isNull(shopId)) {
      throw new AccessTokenUpdateException("Could not execute update because 'shopId' not configured");
    }
    AccessToken currentAccessToken;
    try {
      currentAccessToken = accessTokenStore.findByShopId(shopId);
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenUpdateException("Could not find current access token because store exception, the shop id is " + shopId);
    }
    if (Objects.isNull(currentAccessToken)) {
      throw new AccessTokenUpdateException("Could not find current access token of shop id " + shopId);
    }
    AccessToken accessToken = executeUpdate(currentAccessToken);
    if (Objects.isNull(accessToken)) {
      return;
    }
    if (!AccessTokenHelper.isValid(accessToken)) {
      throw new AccessTokenUpdateException("Could not save invalid access token to store, the shop id is " + shopId)
              .setCurrentAccessToken(currentAccessToken);
    }
    try {
      accessTokenStore.save(accessToken);
    } catch (AccessTokenStoreException e) {
      throw new AccessTokenUpdateException("Could not save access token to store, the shop id is " + shopId)
              .setCurrentAccessToken(currentAccessToken);
    }
    if (Objects.nonNull(accessTokenUpdateListener)) {
      accessTokenUpdateListener.handleUpdated(currentAccessToken, accessToken);
    }
  }

  /**
   * 此方法用于获取新的访问令牌，获取到的访问令牌必须有效
   *
   * @param currentAccessToken 当前的访问令牌不能为空
   * @return 新的访问令牌，可以为空，表示不做任何更新，返回的访问令牌必须有效，有效的访问令牌必需包含访问令牌值、刷新令牌值和店铺编号
   * @throws AccessTokenUpdateException 无法获取新的访问令牌
   * @see AccessTokenHelper#isValid(AccessToken)
   */
  protected abstract AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException;
}
