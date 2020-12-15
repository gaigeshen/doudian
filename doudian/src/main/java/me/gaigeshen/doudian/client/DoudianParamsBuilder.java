package me.gaigeshen.doudian.client;

/**
 * 抖店请求参数构建器，用于将指定的原始数据转换为抖店请求参数
 *
 * @author gaigeshen
 */
public interface DoudianParamsBuilder<S> {
  /**
   * 调用此方法构建抖店请求参数
   *
   * @param method 返回的抖店请求参数对应的远程服务名称，不能为空
   * @param source 指定的原始数据，不能为空
   * @return 抖店请求参数
   * @throws DoudianParamsBuildingException 构建抖店请求参数失败
   */
  DoudianParams build(String method, S source) throws DoudianParamsBuildingException;
}
