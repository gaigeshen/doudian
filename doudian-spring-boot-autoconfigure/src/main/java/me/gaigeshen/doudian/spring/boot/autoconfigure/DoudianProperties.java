package me.gaigeshen.doudian.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author gaigeshen
 */
@ConfigurationProperties("doudian")
public class DoudianProperties {

  private String appKey;

  private String appSecret;

  public String getAppKey() {
    return appKey;
  }

  public void setAppKey(String appKey) {
    this.appKey = appKey;
  }

  public String getAppSecret() {
    return appSecret;
  }

  public void setAppSecret(String appSecret) {
    this.appSecret = appSecret;
  }
}
