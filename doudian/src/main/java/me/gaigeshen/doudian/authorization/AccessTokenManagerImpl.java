package me.gaigeshen.doudian.authorization;

/**
 * @author gaigeshen
 */
public class AccessTokenManagerImpl implements AccessTokenManager {

  private AccessTokenStore accessTokenStore;

  @Override
  public void setAccessTokenStore(AccessTokenStore accessTokenStore) {
    this.accessTokenStore = accessTokenStore;
  }

  @Override
  public void addNewAccessToken(AccessToken accessToken) {

  }

  @Override
  public void deleteAccessToken(String shopId) {

  }

  @Override
  public AccessToken findAccessToken(String shopId) {
    return null;
  }

  /**
   * @author gaigeshen
   */
  private class AccessTokenUpdateTaskImpl extends AbstractAccessTokenUpdateTask {

    @Override
    protected AccessToken executeUpdate(AccessToken currentAccessToken) throws AccessTokenUpdateException {
      return null;
    }
  }

  /**
   * @author gaigeshen
   */
  private class AccessTokenUpadteListenerImpl implements AccessTokenUpdateListener {

    @Override
    public void handleUpdated(AccessToken oldAccessToken, AccessToken newAccessToken) {

    }
  }
}
