package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenRefreshException;
import me.gaigeshen.doudian.authorization.AccessTokenRefresher;
import me.gaigeshen.doudian.client.*;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.client.config.Constants;
import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.Metadata;
import me.gaigeshen.doudian.request.content.MetadataAttributes;
import me.gaigeshen.doudian.request.result.ResultDataTransformerException;
import me.gaigeshen.doudian.util.Asserts;

/**
 * 默认的访问令牌刷新器
 *
 * @author gaigeshen
 */
public class DefaultAccessTokenRefresher implements AccessTokenRefresher {

  private final DoudianClient client;

  /**
   * 创建默认的访问令牌刷新器
   *
   * @param doudianClient 抖店远程服务请求客户端不能为空
   */
  public DefaultAccessTokenRefresher(DoudianClient doudianClient) {
    Asserts.notNull(doudianClient, "doudianClient");
    this.client = doudianClient;
  }

  @Override
  public AccessToken refresh(AccessToken oldAccessToken) throws AccessTokenRefreshException {
    AppConfig appConfig = client.getAppConfig();
    Object[] args = new Object[] { appConfig.getAppKey(), appConfig.getAppSecret(), oldAccessToken.getRefreshToken() };
    AccessTokenData data;
    try {
      data = client.execute(new AccessTokenDataExecution(new ExecuteContent(), args));
    } catch (DoudianExecutionException e) {
      if (e instanceof DoudianExecutionResultException) {
        throw new AccessTokenRefreshException(e).setCurrentAccessToken(oldAccessToken);
      }
      throw new AccessTokenRefreshException(e).setCurrentAccessToken(oldAccessToken).setCanRetry(true);
    }
    try {
      return AccessTokenDataTransformer.getInstance().transform(data);
    } catch (ResultDataTransformerException e) {
      throw new AccessTokenRefreshException("Could not transform to access token from data", e);
    }
  }

  @MetadataAttributes(
          url = Constants.ACCESS_TOKEN_REFRESH_TEMPLATE_URL,
          method = "get",
          requireAccessToken = false,
          type = Metadata.Type.NONE)
  private class ExecuteContent extends AbstractContent<DoudianResult<AccessTokenData>> { }
}
