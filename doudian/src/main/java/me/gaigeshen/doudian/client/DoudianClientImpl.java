package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenRefreshException;
import me.gaigeshen.doudian.authorization.AuthorizationException;

/**
 * @author gaigeshen
 */
public class DoudianClientImpl extends AbstractDoudianClient {
  @Override
  protected AccessToken getAccessToken(String authorizationCode) throws AuthorizationException {
    return null;
  }

  @Override
  protected AccessToken refreshAccessToken(AccessToken oldAccessToken) throws AccessTokenRefreshException {
    return null;
  }

  @Override
  public String getAuthorizeUrl() {
    return null;
  }
}
