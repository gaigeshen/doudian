package me.gaigeshen.doudian.client.accesstoken;

import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.client.AbstractDoudianClient;
import me.gaigeshen.doudian.client.DoudianClientException;
import me.gaigeshen.doudian.client.DoudianResult;
import me.gaigeshen.doudian.config.AppConfig;
import me.gaigeshen.doudian.config.Constants;
import me.gaigeshen.doudian.http.WebClientConfig;
import me.gaigeshen.doudian.request.RequestExecutorException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author gaigeshen
 */
public class DoudianAccessTokenClientImpl extends AbstractDoudianClient implements DoudianAccessTokenClient {

  public DoudianAccessTokenClientImpl(AppConfig appConfig) {
    super(appConfig);
  }

  public DoudianAccessTokenClientImpl(AppConfig appConfig, AccessTokenStore accessTokenStore) {
    super(appConfig, accessTokenStore);
  }

  public DoudianAccessTokenClientImpl(AppConfig appConfig, AccessTokenStore accessTokenStore, WebClientConfig webClientConfig) {
    super(appConfig, accessTokenStore, webClientConfig);
  }

  @Override
  public String getAuthorizeUrl(String redirectUri) {
    return String.format(Constants.AUTHORIZE_TEMPLATE_URL, getAppConfig().getAppKey(), redirectUri, "");
  }

  @Override
  public AccessTokenData getAccessToken(String authorizationCode) {
    AppConfig appConfig = getAppConfig();
    DoudianResult<AccessTokenData> result;
    try {
      result = execute(new AccessTokenContent(), new Object[] { appConfig.getAppKey(), appConfig.getAppSecret(), authorizationCode });
    } catch (RequestExecutorException e) {
      throw new DoudianClientException("Could not get access token with authorization code " + authorizationCode, e);
    }
    if (result.failed()) {
      throw new DoudianClientException("Could not get access token with authorization code " + authorizationCode
              + ", reason is " + StringUtils.defaultString(result.getMessage()));
    }
    return result.getData();
  }

  @Override
  public AccessTokenData refreshAccessToken(String refreshToken) {
    AppConfig appConfig = getAppConfig();
    DoudianResult<AccessTokenData> result;
    try {
      result = execute(new AccessTokenRefreshContent(), new Object[] { appConfig.getAppKey(), appConfig.getAppSecret(), refreshToken });
    } catch (RequestExecutorException e) {
      throw new DoudianClientException("Could not refresh access token with refresh token " + refreshToken, e);
    }
    if (result.failed()) {
      throw new DoudianClientException("Could not refresh access token with refresh token " + refreshToken
              + ", reason is " + StringUtils.defaultString(result.getMessage()));
    }
    return result.getData();
  }
}
