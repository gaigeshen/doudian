package me.gaigeshen.doudian.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;

/**
 * 抖店配置
 *
 * @author gaigeshen
 */
@ConfigurationProperties("doudian")
public class DoudianProperties {
  /**
   * 应用编号
   */
  private String appKey;
  /**
   * 应用密钥
   */
  private String appSecret;
  /**
   * 请求执行器配置
   */
  private WebClient webClient = new WebClient();
  /**
   * 数据源配置
   */
  private Jdbc jdbc = new Jdbc();
  /**
   * 访问令牌存储器类型
   */
  private AccessTokenStoreType accessTokenStoreType = AccessTokenStoreType.MEMORY;
  /**
   * 是否启用授权流程处理，如果启用则会开启授权流程的支持，同时也会开启访问令牌管理器
   */
  private boolean enableAuthorization = false;

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

  public WebClient getWebClient() {
    return webClient;
  }

  public void setWebClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public Jdbc getJdbc() {
    return jdbc;
  }

  public void setJdbc(Jdbc jdbc) {
    this.jdbc = jdbc;
  }

  public AccessTokenStoreType getAccessTokenStoreType() {
    return accessTokenStoreType;
  }

  public void setAccessTokenStoreType(AccessTokenStoreType accessTokenStoreType) {
    this.accessTokenStoreType = accessTokenStoreType;
  }

  public boolean isEnableAuthorization() {
    return enableAuthorization;
  }

  public void setEnableAuthorization(boolean enableAuthorization) {
    this.enableAuthorization = enableAuthorization;
  }

  /**
   * 请求执行器配置
   *
   * @author gaigeshen
   */
  public static class WebClient {

    private int connectionRequestTimeout = 1000;

    private int connectTimeout = 2000;

    private int socketTimeout = 5000;

    public int getConnectionRequestTimeout() {
      return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
      this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectTimeout() {
      return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
      this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
      return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
      this.socketTimeout = socketTimeout;
    }
  }

  /**
   * 数据源配置
   *
   * @author gaigeshen
   */
  public static class Jdbc {

    private static final String DEFAULT_SCHEMA_LOCATION = "classpath:me/gaigeshen/doudian/jdbc/tables_@@platform@@.sql";

    /**
     * 将用于数据源初始化，执行脚本文件
     */
    private String schema = DEFAULT_SCHEMA_LOCATION;
    /**
     * 数据源初始化模式，默认只在嵌入式的数据源进行初始化
     */
    private DataSourceInitializationMode initializeSchema = DataSourceInitializationMode.EMBEDDED;

    public String getSchema() {
      return schema;
    }

    public void setSchema(String schema) {
      this.schema = schema;
    }

    public DataSourceInitializationMode getInitializeSchema() {
      return initializeSchema;
    }

    public void setInitializeSchema(DataSourceInitializationMode initializeSchema) {
      this.initializeSchema = initializeSchema;
    }
  }
}
