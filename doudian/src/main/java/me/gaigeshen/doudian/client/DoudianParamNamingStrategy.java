package me.gaigeshen.doudian.client;

import java.util.Objects;

/**
 * 抖店请求参数命名策略
 *
 * @author gaigeshen
 */
public interface DoudianParamNamingStrategy {

  /**
   * 小写字母加下划线的形式
   */
  DoudianParamNamingStrategy SNAKE_CASE = new SnakeCaseStrategy();

  /**
   * 首字母大写的形式
   */
  DoudianParamNamingStrategy UPPER_CAMEL_CASE = new UpperCamelCaseStrategy();

  /**
   * 全部转成小写的形式
   */
  DoudianParamNamingStrategy LOWER_CASE = new LowerCaseStrategy();

  /**
   * 返回抖店请求参数名称
   *
   * @param defaultName 默认的抖店请求参数名称，即原始字段的名称
   * @return 抖店请求参数名称
   */
  default String getName(String defaultName) {
    return defaultName;
  }

  class SnakeCaseStrategy implements DoudianParamNamingStrategy {
    @Override
    public String getName(String defaultName) {
      if (Objects.isNull(defaultName)) {
        return null;
      }
      int length = defaultName.length();
      StringBuilder result = new StringBuilder(length * 2);
      int resultLength = 0;
      boolean wasPrevTranslated = false;
      for (int i = 0; i < length; i++) {
        char c = defaultName.charAt(i);
        if (i > 0 || c != '_') {
          if (Character.isUpperCase(c)) {
            if (!wasPrevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != '_') {
              result.append('_');
              resultLength++;
            }
            c = Character.toLowerCase(c);
            wasPrevTranslated = true;
          } else {
            wasPrevTranslated = false;
          }
          result.append(c);
          resultLength++;
        }
      }
      return resultLength > 0 ? result.toString() : defaultName;
    }
  }

  class UpperCamelCaseStrategy implements DoudianParamNamingStrategy {
    @Override
    public String getName(String defaultName) {
      if (defaultName == null || defaultName.length() == 0) {
        return defaultName;
      }
      char c = defaultName.charAt(0);
      char uc = Character.toUpperCase(c);
      if (c == uc) {
        return defaultName;
      }
      StringBuilder sb = new StringBuilder(defaultName);
      sb.setCharAt(0, uc);
      return sb.toString();
    }
  }

  class LowerCaseStrategy implements DoudianParamNamingStrategy {
    @Override
    public String getName(String defaultName) {
      return defaultName.toLowerCase();
    }
  }
}
