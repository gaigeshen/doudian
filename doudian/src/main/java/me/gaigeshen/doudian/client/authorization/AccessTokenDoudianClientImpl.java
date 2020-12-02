package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.client.AbstractDoudianClient;
import me.gaigeshen.doudian.client.DoudianClientException;
import me.gaigeshen.doudian.client.config.AppConfig;
import org.apache.commons.lang3.StringUtils;

import static me.gaigeshen.doudian.client.config.Constants.AUTHORIZE_TEMPLATE_URL;

/**
 * 抖店访问令牌客户端实现
 *
 * @author gaigeshen
 */
public class AccessTokenDoudianClientImpl extends AbstractDoudianClient implements AccessTokenDoudianClient {

  @Override
  public String getAuthorizeUrl() {
    return String.format(AUTHORIZE_TEMPLATE_URL, getAppConfig().getAppKey(), "");
  }

  @Override
  public AccessTokenData getAccessToken(String authorizationCode) throws DoudianClientException {
    if (StringUtils.isBlank(authorizationCode)) {
      throw new DoudianClientException("'authorizationCode' cannot be null or blank");
    }
    AppConfig appConfig = getAppConfig();
    Object[] urlValues = new Object[] { appConfig.getAppKey(), appConfig.getAppSecret(), authorizationCode };
    return execute(new AccessTokenContent(), urlValues);
  }

  @Override
  public AccessTokenData refreshAccessToken(String refreshToken) throws DoudianClientException {
    if (StringUtils.isBlank(refreshToken)) {
      throw new DoudianClientException("'refreshToken' cannot be null or blank");
    }
    AppConfig appConfig = getAppConfig();
    Object[] urlValues = new Object[] { appConfig.getAppKey(), appConfig.getAppSecret(), refreshToken };
    return execute(new AccessTokenRefreshContent(), urlValues);
  }
}
