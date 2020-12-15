package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.util.Asserts;

import java.util.Map;

/**
 * 抖店请求参数构建器实现
 *
 * @author gaigeshen
 */
public class DoudianParamsBuilderFormMapImpl implements DoudianParamsBuilder<Map<String, Object>> {

  private static final DoudianParamsBuilderFormMapImpl INSTANCE = new DoudianParamsBuilderFormMapImpl();

  public static DoudianParamsBuilderFormMapImpl getInstance() {
    return INSTANCE;
  }

  @Override
  public DoudianParams build(String method, Map<String, Object> source) {
    Asserts.notBlank(method, "method");
    Asserts.notNull(source, "source");

    DoudianParams params = new DoudianParams(method);
    params.addParams(source);
    return params;
  }
}
