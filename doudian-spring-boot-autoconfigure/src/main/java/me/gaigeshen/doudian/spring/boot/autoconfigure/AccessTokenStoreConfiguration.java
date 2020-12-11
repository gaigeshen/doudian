package me.gaigeshen.doudian.spring.boot.autoconfigure;

import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.authorization.AccessTokenStoreImpl;
import me.gaigeshen.doudian.authorization.AccessTokenStoreJdbcImpl;
import me.gaigeshen.doudian.client.DoudianClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;

/**
 * 访问令牌存储器配置
 *
 * @author gaigeshen
 */
@ConditionalOnSingleCandidate(DataSource.class)
@Configuration
public class AccessTokenStoreConfiguration {

  @ConditionalOnMissingBean
  @Bean
  public AccessTokenStore accessTokenStore(DataSource dataSource,
                                           @DoudianDataSource ObjectProvider<DataSource> doudianDataSource,
                                           DoudianProperties properties) {
    AccessTokenStoreType accessTokenStoreType = properties.getAccessTokenStoreType();
    if (accessTokenStoreType == AccessTokenStoreType.JDBC) {
      return new AccessTokenStoreJdbcImpl(getDataSource(dataSource, doudianDataSource));
    }
    return new AccessTokenStoreImpl();
  }

  @ConditionalOnMissingBean
  @Bean
  public DoudianDataSourceInitializer doudianDataSourceInitializer(
          DataSource dataSource, @DoudianDataSource ObjectProvider<DataSource> doudianDataSource,
          ResourceLoader resourceLoader, DoudianProperties properties) {
    return new DoudianDataSourceInitializer(getDataSource(dataSource, doudianDataSource), resourceLoader, properties);
  }

  private DataSource getDataSource(DataSource dataSource, ObjectProvider<DataSource> doudianDataSource) {
    DataSource dataSourceIfAvailable = doudianDataSource.getIfAvailable();
    return (dataSourceIfAvailable != null) ? dataSourceIfAvailable : dataSource;
  }

  @Bean
  public static DataSourceInitializerDoudianDependencyPostProcessor dataSourceInitializerDoudianDependencyPostProcessor() {
    return new DataSourceInitializerDoudianDependencyPostProcessor();
  }

  private static class DataSourceInitializerDoudianDependencyPostProcessor extends AbstractDependsOnBeanFactoryPostProcessor {
    public DataSourceInitializerDoudianDependencyPostProcessor() {
      super(DoudianClient.class, "doudianDataSourceInitializer");
    }
  }
}