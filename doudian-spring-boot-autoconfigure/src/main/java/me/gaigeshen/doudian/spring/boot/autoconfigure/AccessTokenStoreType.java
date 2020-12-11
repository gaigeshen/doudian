package me.gaigeshen.doudian.spring.boot.autoconfigure;

/**
 * 访问令牌存储器类型
 *
 * @author gaigeshen
 */
public enum AccessTokenStoreType {
  /**
   * 基于内存的
   *
   * @see me.gaigeshen.doudian.authorization.AccessTokenStoreImpl
   */
  MEMORY,

  /**
   * 基于数据源的，请确保已经配置了正确的数据源，否则此选项不生效
   *
   * @see DoudianDataSource
   * @see me.gaigeshen.doudian.authorization.AccessTokenStoreJdbcImpl
   */
  JDBC
}
