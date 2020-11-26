package me.gaigeshen.doudian.client;

import lombok.Data;
import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenExchangeException;
import me.gaigeshen.doudian.authorization.AccessTokenExchanger;
import me.gaigeshen.doudian.authorization.AccessTokenRefreshException;
import me.gaigeshen.doudian.config.AppConfig;
import me.gaigeshen.doudian.config.Constants;
import me.gaigeshen.doudian.request.RequestExecutor;
import me.gaigeshen.doudian.request.RequestExecutorException;
import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.Metadata;
import me.gaigeshen.doudian.request.content.MetadataAttributes;
import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Objects;

/**
 * 访问令牌换取器实现
 *
 * @author gaigeshen
 */
public class AccessTokenExchangerImpl implements AccessTokenExchanger {

  private final AppConfig appConfig;

  private final RequestExecutor requestExecutor;

  public AccessTokenExchangerImpl(AppConfig appConfig, RequestExecutor requestExecutor) {
    this.appConfig = Asserts.notNull(appConfig, "appConfig");
    this.requestExecutor = Asserts.notNull(requestExecutor, "requestExecutor");
  }

  @Override
  public AccessToken get(String authorizationCode) throws AccessTokenExchangeException {
    DoudianResult<AccessTokenData> result;
    try {
      result = requestExecutor.execute(new AccessTokenContent(), appConfig.getAppKey(), appConfig.getAppSecret(), authorizationCode);
    } catch (RequestExecutorException e) {
      throw new AccessTokenExchangeException("Could not get access token with authorization code " + authorizationCode, e);
    }
    if (!result.isSuccessResult()) {
      throw new AccessTokenExchangeException("Could not get access token with authorization code " + authorizationCode
              + ", reason is " + result.getMessage());
    }
    AccessTokenData data = result.getData();
    if (StringUtils.isAnyBlank(data.getAccessToken(), data.getRefreshToken(), data.getShopId())) {
      throw new AccessTokenExchangeException("The access token has no 'accessToken' or 'refreshToken' or 'shopId'");
    }
    if (Objects.isNull(data.getExpiresIn())) {
      throw new AccessTokenExchangeException("The access token has no 'expiresIn'");
    }
    return parseAccessToken(data);
  }

  @Override
  public AccessToken refresh(AccessToken oldAccessToken) throws AccessTokenRefreshException {
    DoudianResult<AccessTokenData> result;
    try {
      result = requestExecutor.execute(new AccessTokenRefreshContent(), appConfig.getAppKey(), appConfig.getAppSecret(), oldAccessToken.getRefreshToken());
    } catch (RequestExecutorException e) {
      throw new AccessTokenRefreshException("Could not refresh access token from current access token " + oldAccessToken, e)
              .setCurrentAccessToken(oldAccessToken).setCanRetry(true);
    }
    if (!result.isSuccessResult()) {
      throw new AccessTokenRefreshException("Could not refresh access token from current access token " + oldAccessToken
              + ", reason is " + result.getMessage()).setCurrentAccessToken(oldAccessToken);
    }
    AccessTokenData data = result.getData();
    if (StringUtils.isAnyBlank(data.getAccessToken(), data.getRefreshToken(), data.getShopId())) {
      throw new AccessTokenRefreshException("The access token refreshed has no 'accessToken' or 'refreshToken' or 'shopId'")
              .setCurrentAccessToken(oldAccessToken);
    }
    if (Objects.isNull(data.getExpiresIn())) {
      throw new AccessTokenRefreshException("The access token refreshed has no 'expiresIn'")
              .setCurrentAccessToken(oldAccessToken);
    }
    return parseAccessToken(data);
  }

  private AccessToken parseAccessToken(AccessTokenData data) {
    return AccessToken.builder()
            .setAccessToken(data.getAccessToken()).setRefreshToken(data.getRefreshToken())
            .setScope(data.getScope())
            .setShopId(data.getShopId()).setShopName(data.getShopName())
            .setExpiresIn(data.getExpiresIn())
            .setExpiresTimestamp(System.currentTimeMillis() / 1000 + data.getExpiresIn())
            .setUpdateTime(new Date())
            .build();
  }

  /**
   * 访问令牌请求数据
   *
   * @author gaigeshen
   */
  @MetadataAttributes(
          url = Constants.ACCESS_TOKEN_TEMPLATE_URL,
          method = "get",
          requireAccessToken = false,
          type = Metadata.Type.NONE
  )
  public static class AccessTokenContent extends AbstractContent<DoudianResult<AccessTokenData>> {
  }

  /**
   * 刷新访问令牌请求数据
   *
   * @author gaigeshen
   */
  @MetadataAttributes(
          url = Constants.ACCESS_TOKEN_REFRESH_TEMPLATE_URL,
          method = "get",
          requireAccessToken = false,
          type = Metadata.Type.NONE
  )
  public static class AccessTokenRefreshContent extends AbstractContent<DoudianResult<AccessTokenData>> {
  }

  /**
   * 访问令牌请求响应数据
   *
   * @author gaigeshen
   */
  @Data
  public class AccessTokenData {

    private String accessToken;

    private String refreshToken;

    private String scope;

    private String shopId;

    private String shopName;

    private Long expiresIn;
  }
}
