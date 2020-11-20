package me.gaigeshen.doudian.http;

import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.net.ssl.SSLContext;

/**
 * Configuration for web client
 *
 * @author gaigeshen
 */
public class WebClientConfig {
  private final int connectionRequestTimeout;
  private final int connectTimeout;
  private final int socketTimeout;
  private final SSLContext sslContext;

  private WebClientConfig(Builder builder) {
    this.connectionRequestTimeout = builder.connectionRequestTimeout;
    this.connectTimeout = builder.connectTimeout;
    this.socketTimeout = builder.socketTimeout;
    this.sslContext = builder.sslContext;
  }

  /**
   * Returns default configuration
   *
   * @return The default configuration
   */
  public static WebClientConfig getDefault() {
    return builder().build();
  }

  /**
   * Returns default configuration builder
   *
   * @return The builder
   */
  public static Builder builder() {
    return new Builder();
  }

  public int getConnectionRequestTimeout() {
    return connectionRequestTimeout;
  }

  public int getConnectTimeout() {
    return connectTimeout;
  }

  public int getSocketTimeout() {
    return socketTimeout;
  }

  public SSLContext getSslContext() {
    return sslContext;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  /**
   * Builder class
   *
   * @author gaigeshen
   */
  public static class Builder {
    private int connectionRequestTimeout = 1000;
    private int connectTimeout = 2000;
    private int socketTimeout = 5000;
    private SSLContext sslContext = null;

    public Builder setConnectionRequestTimeout(int connectionRequestTimeout) {
      this.connectionRequestTimeout = Asserts.positive(connectionRequestTimeout, "connectionRequestTimeout");
      return this;
    }

    public Builder setConnectTimeout(int connectTimeout) {
      this.connectTimeout = Asserts.positive(connectTimeout, "connectTimeout");
      return this;
    }

    public Builder setSocketTimeout(int socketTimeout) {
      this.socketTimeout = Asserts.positive(socketTimeout, "socketTimeout");
      return this;
    }

    public Builder setSslContext(SSLContext sslContext) {
      this.sslContext = sslContext;
      return this;
    }

    public WebClientConfig build() {
      return new WebClientConfig(this);
    }
  }
}
