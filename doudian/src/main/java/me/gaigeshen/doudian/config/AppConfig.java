package me.gaigeshen.doudian.config;

import org.apache.commons.lang3.StringUtils;

/**
 * 应用配置
 *
 * @author gaigeshen
 */
public class AppConfig {
  private final String appKey;
  private final String appSecret;

  public AppConfig(String appKey, String appSecret) {
    if (StringUtils.isBlank(appKey) || StringUtils.isBlank(appSecret)) {
      throw new IllegalArgumentException("appKey and appSecret cannot be null or blank");
    }
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
