package me.gaigeshen.doudian.client.accesstoken;

import me.gaigeshen.doudian.client.DoudianClient;

/**
 * @author gaigeshen
 */
public interface DoudianAccessTokenClient extends DoudianClient {

  String getAuthorizeUrl(String redirectUri);

  AccessTokenData getAccessToken(String authorizationCode);

  AccessTokenData refreshAccessToken(String refreshToken);
}
