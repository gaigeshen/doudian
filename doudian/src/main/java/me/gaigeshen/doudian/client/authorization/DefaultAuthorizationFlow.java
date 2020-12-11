package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.AbstractAuthorizationFlow;
import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenManager;
import me.gaigeshen.doudian.authorization.AuthorizationException;
import me.gaigeshen.doudian.client.DoudianClient;
import me.gaigeshen.doudian.client.DoudianDataTransformerException;
import me.gaigeshen.doudian.client.DoudianExecutionException;
import me.gaigeshen.doudian.client.DoudianResult;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.client.config.Constants;
import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.Metadata;
import me.gaigeshen.doudian.request.content.MetadataAttributes;
import me.gaigeshen.doudian.util.Asserts;

/**
 * 默认的授权流程处理
 *
 * @author gaigeshen
 */
public class DefaultAuthorizationFlow extends AbstractAuthorizationFlow {

  private final AccessTokenManager accessTokenManager;

  private final DoudianClient client;

  /**
   * 创建默认的授权流程处理
   *
   * @param accessTokenManager 访问令牌存储器不能为空，用于授权流程处理
   * @param doudianClient 抖店远程服务请求客户端不能为空
   */
  public DefaultAuthorizationFlow(AccessTokenManager accessTokenManager, DoudianClient doudianClient) {
    Asserts.notNull(accessTokenManager, "accessTokenManager");
    Asserts.notNull(doudianClient, "doudianClient");
    this.accessTokenManager = accessTokenManager;
    this.client = doudianClient;
  }

  @Override
  protected AccessTokenManager getAccessTokenManager() {
    return accessTokenManager;
  }

  @Override
  protected AccessToken getAccessToken(String authorizationCode) throws AuthorizationException {
    AppConfig appConfig = client.getAppConfig();
    Object[] args = new Object[] { appConfig.getAppKey(), appConfig.getAppSecret(), authorizationCode };
    AccessTokenData data;
    try {
      data = client.execute(new AccessTokenDataExecution(new ExecuteContent(), args));
    } catch (DoudianExecutionException e) {
      throw new AuthorizationException("Could not get access token with authorization code " + authorizationCode, e);
    }
    try {
      return AccessTokenDataTransformer.getInstance().transform(data);
    } catch (DoudianDataTransformerException e) {
      throw new AuthorizationException("Could not transform to access token from data", e);
    }
  }

  @Override
  public String getAuthorizeUrl() {
    return String.format(Constants.AUTHORIZE_TEMPLATE_URL, client.getAppConfig().getAppKey(), "");
  }

  @MetadataAttributes(
          url = Constants.ACCESS_TOKEN_TEMPLATE_URL,
          method = "get",
          requireAccessToken = false,
          type = Metadata.Type.NONE)
  private class ExecuteContent extends AbstractContent<DoudianResult<AccessTokenData>> { }
}
