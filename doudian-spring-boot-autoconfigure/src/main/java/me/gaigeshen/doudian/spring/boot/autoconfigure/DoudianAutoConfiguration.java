package me.gaigeshen.doudian.spring.boot.autoconfigure;

import me.gaigeshen.doudian.authorization.*;
import me.gaigeshen.doudian.client.DefaultDoudianClient;
import me.gaigeshen.doudian.client.DoudianClient;
import me.gaigeshen.doudian.client.authorization.DefaultAccessTokenRefresher;
import me.gaigeshen.doudian.client.authorization.DefaultAuthorizationFlow;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.http.WebClientConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 抖店自动配置
 *
 * @author gaigeshen
 */
@ConditionalOnClass(DoudianClient.class)
@EnableConfigurationProperties(DoudianProperties.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class })
@Import({ AccessTokenStoreConfiguration.class })
@Configuration
public class DoudianAutoConfiguration {

  private final DoudianProperties properties;

  public DoudianAutoConfiguration(DoudianProperties properties) {
    this.properties = properties;
  }

  @Bean(destroyMethod = "close")
  public DoudianClient doudianClient(AccessTokenStore accessTokenStore) throws Exception {
    AppConfig appConfig = new AppConfig(properties.getAppKey(), properties.getAppSecret());
    WebClientConfig webClientConfig = WebClientConfig.builder()
            .setConnectionRequestTimeout(properties.getWebClient().getConnectionRequestTimeout())
            .setConnectTimeout(properties.getWebClient().getConnectTimeout())
            .setSocketTimeout(properties.getWebClient().getSocketTimeout())
            .build();
    return new DefaultDoudianClient(appConfig, accessTokenStore, webClientConfig);
  }

  @ConditionalOnMissingBean
  @Bean
  public AccessTokenStore accessTokenStore() {
    return new AccessTokenStoreImpl();
  }

  @ConditionalOnProperty(prefix = "doudian", name = "enable-authorization")
  @Configuration
  protected static class AuthorizationFlowConfiguration {

    @Bean
    public AuthorizationFlow authorizationFlow(AccessTokenManager accessTokenManager,
                                               DoudianClient doudianClient) {
      return new DefaultAuthorizationFlow(accessTokenManager, doudianClient);
    }

    @Bean(destroyMethod = "shutdown")
    public AccessTokenManager accessTokenManager(AccessTokenStore accessTokenStore,
                                                 AccessTokenRefresher accessTokenRefresher) throws Exception {
      return new AccessTokenManagerImpl(accessTokenStore, accessTokenRefresher);
    }

    @Bean
    public AccessTokenRefresher accessTokenRefresher(DoudianClient doudianClient) {
      return new DefaultAccessTokenRefresher(doudianClient);
    }
  }
}
