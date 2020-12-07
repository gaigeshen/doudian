package me.gaigeshen.doudian.client;

/**
 * 抖店请求参数字符串序列化器
 *
 * @author gaigeshen
 */
public interface DoudianParamsStringify {

  /**
   * 字符串化抖店请求参数
   *
   * @param params 抖店请求参数不能为空
   * @return 序列化结果字符串
   */
  default String parseString(DoudianParams params) {
    return parseString(params, false);
  }

  /**
   * 字符串化抖店请求参数
   *
   * @param params 抖店请求参数不能为空
   * @param paramValueStringify 是否将所有的请求参数值转换为字符串处理
   * @return 序列化结果字符串
   */
  String parseString(DoudianParams params, boolean paramValueStringify);

}
