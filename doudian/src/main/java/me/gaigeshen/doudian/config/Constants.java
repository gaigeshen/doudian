package me.gaigeshen.doudian.config;

/**
 * 常量
 *
 * @author gaigeshen
 */
public class Constants {

  /** 接口请求地址 */
  public static final String API_URL = "https://openapi-fxg.jinritemai.com";

  /** 店铺授权地址 */
  public static final String AUTHORIZE_TEMPLATE_URL = "https://fxg.jinritemai.com/index.html#/ffa/open/applicationAuthorize?response_type=code&app_id=%s&redirect_uri=%s&state=%s";

  /** 访问令牌地址 */
  public static final String ACCESS_TOKEN_TEMPLATE_URL = "https://openapi-fxg.jinritemai.com/oauth2/access_token?app_id=%s&app_secret=%s&code=%s&grant_type=authorization_code";

  /** 刷新访问令牌地址 */
  public static final String ACCESS_TOKEN_REFRESH_TEMPLATE_URL = "https://openapi-fxg.jinritemai.com/oauth2/refresh_token?app_id=%s&app_secret=%s&grant_type=refresh_token&refresh_token=%s";

  private Constants() { }

}
