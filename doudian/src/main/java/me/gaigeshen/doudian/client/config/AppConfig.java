package me.gaigeshen.doudian.client.config;

import me.gaigeshen.doudian.util.Asserts;

/**
 * 应用配置
 *
 * @author gaigeshen
 */
public class AppConfig {

  private final String appKey;

  private final String appSecret;

  /**
   * 创建应用配置
   *
   * @param appKey 应用编号不能为空
   * @param appSecret 应用密钥不能为空
   */
  public AppConfig(String appKey, String appSecret) {
    Asserts.notBlank(appKey, "appKey");
    Asserts.notBlank(appSecret, "appSecret");
    this.appKey = appKey;
    this.appSecret = appSecret;
  }

  /**
   * 返回应用编号
   *
   * @return 应用编号
   */
  public String getAppKey() {
    return appKey;
  }

  /**
   * 返回应用密钥
   *
   * @return 应用密钥
   */
  public String getAppSecret() {
    return appSecret;
  }
}
