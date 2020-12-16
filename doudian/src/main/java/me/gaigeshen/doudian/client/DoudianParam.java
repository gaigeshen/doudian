package me.gaigeshen.doudian.client;

import java.lang.annotation.*;

/**
 * 此注解用在抖店请求参数来源字段上，来表达某个抖店请求参数
 *
 * @author gaigeshen
 * @see DoudianParamsBuilderFromSourceImpl
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoudianParam {
  /**
   * 参数的名称，如果为空或者空字符串，则取命名策略
   *
   * @return 参数的名称
   */
  String value();

  /**
   * 返回命名策略，默认小写字母加下划线的形式
   *
   * @return 命名策略
   */
  NamingStrategy namingStrategy() default NamingStrategy.SNAKE_CASE;

  /**
   * 命名策略，目前仅支持三种
   */
  enum NamingStrategy {
    SNAKE_CASE(DoudianParamNamingStrategy.SNAKE_CASE),
    UPPER_CAMEL_CASE(DoudianParamNamingStrategy.UPPER_CAMEL_CASE),
    LOWER_CASE(DoudianParamNamingStrategy.LOWER_CASE);

    private final DoudianParamNamingStrategy strategy;

    NamingStrategy(DoudianParamNamingStrategy strategy) {
      this.strategy = strategy;
    }

    /**
     * 返回抖店请求参数命名策略
     *
     * @return 抖店请求参数命名策略
     */
    public DoudianParamNamingStrategy getStrategy() {
      return strategy;
    }

    /**
     * 使用抖店请求参数命名策略来返回抖店请求参数名称
     *
     * @param defaultName 默认的抖店请求参数名称
     * @return 抖店请求参数名称
     */
    public String getName(String defaultName) {
      return getStrategy().getName(defaultName);
    }
  }
}
