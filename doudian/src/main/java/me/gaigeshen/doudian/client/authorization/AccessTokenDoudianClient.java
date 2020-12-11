package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.*;
import me.gaigeshen.doudian.client.*;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.request.RequestExecutionException;
import me.gaigeshen.doudian.request.RequestExecutionResultException;
import me.gaigeshen.doudian.request.RequestExecutor;
import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.content.Metadata;
import me.gaigeshen.doudian.request.content.MetadataAttributes;
import me.gaigeshen.doudian.request.result.AbstractResult;
import me.gaigeshen.doudian.util.Asserts;

/**
 * 抖店远程服务请求客户端，专门用于获取访问令牌或者刷新访问令牌的，同时也支持授权流程处理
 *
 * @author gaigeshen
 */
public class AccessTokenDoudianClient extends AbstractAuthorizationFlow implements AuthorizationFlow, AccessTokenRefresher {

  private static final String AUTHORIZE_TEMPLATE_URL = "https://fuwu.jinritemai.com/authorize?service_id=%s&state=%s";

  private static final String ACCESS_TOKEN_TEMPLATE_URL = "https://openapi-fxg.jinritemai.com/oauth2/access_token?app_id=%s&app_secret=%s&code=%s&grant_type=authorization_code";

  private static final String ACCESS_TOKEN_REFRESH_TEMPLATE_URL = "https://openapi-fxg.jinritemai.com/oauth2/refresh_token?app_id=%s&app_secret=%s&grant_type=refresh_token&refresh_token=%s";

  private final AccessTokenManager accessTokenManager;

  private final DoudianClient client;

  /**
   * 创建此对象
   *
   * @param accessTokenManager 访问令牌存储器不能为空，用于授权流程处理
   * @param doudianClient 抖店远程服务请求客户端不能为空，获取或者刷新访问令牌需要用到
   */
  public AccessTokenDoudianClient(AccessTokenManager accessTokenManager, DoudianClient doudianClient) {
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
    return String.format(AUTHORIZE_TEMPLATE_URL, client.getAppConfig().getAppKey(), "");
  }

  @Override
  public AccessToken refresh(AccessToken oldAccessToken) throws AccessTokenRefreshException {
    AppConfig appConfig = client.getAppConfig();
    Object[] args = new Object[] { appConfig.getAppKey(), appConfig.getAppSecret(), oldAccessToken.getRefreshToken() };
    AccessTokenData data;
    try {
      data = client.execute(new AccessTokenDataExecution(new RefreshExecuteContent(), args));
    } catch (DoudianExecutionException e) {
      if (e instanceof DoudianExecutionResultException) {
        throw new AccessTokenRefreshException(e).setCurrentAccessToken(oldAccessToken);
      }
      throw new AccessTokenRefreshException(e).setCurrentAccessToken(oldAccessToken).setCanRetry(true);
    }
    try {
      return AccessTokenDataTransformer.getInstance().transform(data);
    } catch (DoudianDataTransformerException e) {
      throw new AccessTokenRefreshException("Could not transform to access token from data", e);
    }
  }

  /**
   * 抖店远程服务请求过程用于获取访问令牌
   *
   * @author gaigeshen
   */
  private class AccessTokenDataExecution implements DoudianClient.Execution<AccessTokenData> {

    private final Content<? extends AbstractResult<AccessTokenData>> content;

    private final Object[] args;

    public AccessTokenDataExecution(Content<? extends AbstractResult<AccessTokenData>> content, Object[] args) {
      this.content = content;
      this.args = args;
    }

    @Override
    public AccessTokenData execute(RequestExecutor executor) throws DoudianExecutionException {
      try {
        return executor.executeForData(content, args);
      } catch (RequestExecutionException e) {
        if (e instanceof RequestExecutionResultException) {
          throw new DoudianExecutionResultException(e).setContent(e.getContent()).setResult(((RequestExecutionResultException) e).getResult());
        }
        throw new DoudianExecutionException(e).setContent(e.getContent());
      }
    }
  }

  /**
   * 通过授权码获取访问令牌
   *
   * @author gaigeshen
   */
  @MetadataAttributes(url = ACCESS_TOKEN_TEMPLATE_URL, method = "get", requireAccessToken = false, type = Metadata.Type.NONE)
  private class ExecuteContent extends AbstractContent<DoudianResult<AccessTokenData>> { }

  /**
   * 通过刷新令牌获取访问令牌
   *
   * @author gaigeshen
   */
  @MetadataAttributes(url = ACCESS_TOKEN_REFRESH_TEMPLATE_URL, method = "get", requireAccessToken = false, type = Metadata.Type.NONE)
  private class RefreshExecuteContent extends AbstractContent<DoudianResult<AccessTokenData>> { }
}
