package me.gaigeshen.doudian.spring.boot.autoconfigure;

import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.authorization.AccessTokenStoreImpl;
import me.gaigeshen.doudian.client.DefaultDoudianClient;
import me.gaigeshen.doudian.client.DoudianClient;
import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.http.WebClientConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

  @ConditionalOnMissingBean
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
}
