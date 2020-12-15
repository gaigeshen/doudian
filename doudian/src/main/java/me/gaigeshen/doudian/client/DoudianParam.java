package me.gaigeshen.doudian.client;

import java.lang.annotation.*;

/**
 * 此注解用在字段上，来表达某个抖店请求参数
 *
 * @author gaigeshen
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoudianParam {
  /**
   * 参数的名称，如果为空或者空字符串，则取字段的原始名称
   *
   * @return 参数的名称
   */
  String value();
}
