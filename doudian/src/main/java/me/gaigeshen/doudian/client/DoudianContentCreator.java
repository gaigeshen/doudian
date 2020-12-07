package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.client.config.AppConfig;

/**
 * 抖店请求数据内容创建器，用于创建抖店请求数据内容对象
 *
 * @author gaigeshen
 */
public interface DoudianContentCreator {
  /**
   * 创建抖店请求数据内容
   *
   * @param params 抖店请求数据内容里面的参数集合，不能为空
   * @param appConfig 需要应用配置，不能为空
   * @param <D> 抖店请求执行结果中数据部分的类型
   * @return 抖店请求数据内容
   */
  <D> DoudianContent<D> create(DoudianParams params, AppConfig appConfig);

}