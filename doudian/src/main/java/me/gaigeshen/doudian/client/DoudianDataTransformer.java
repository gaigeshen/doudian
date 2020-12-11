package me.gaigeshen.doudian.client;

/**
 * 抖店请求执行结果数据转换器，用于将该数据转换成指定类型的对象
 *
 * @author gaigeshen
 */
public interface DoudianDataTransformer<S extends DoudianData, R> {

  /**
   * 转换方法
   *
   * @param data 抖店请求执行结果数据，不能为空
   * @return 转换后的结果对象，不能为空
   * @throws DoudianDataTransformerException 转换发生异常无法转换
   */
  R transform(S data) throws DoudianDataTransformerException;
}
