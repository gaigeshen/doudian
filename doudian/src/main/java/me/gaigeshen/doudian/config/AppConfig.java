package me.gaigeshen.doudian.config;

import me.gaigeshen.doudian.util.Asserts;

/**
 * 应用配置
 *
 * @author gaigeshen
 */
public class AppConfig {
  private final String appKey;
  private final String appSecret;

  public AppConfig(String appKey, String appSecret) {
    this.appKey = Asserts.notBlank(appKey, "appKey");
    this.appSecret = Asserts.notBlank(appSecret, "appSecret");
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
