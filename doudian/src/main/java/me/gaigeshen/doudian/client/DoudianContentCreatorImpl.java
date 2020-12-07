package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.client.config.AppConfig;
import me.gaigeshen.doudian.util.Asserts;
import me.gaigeshen.doudian.util.TimestampUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 抖店请求数据内容创建器实现
 *
 * @author gaigeshen
 */
public class DoudianContentCreatorImpl implements DoudianContentCreator {

  private final DoudianParamsStringify paramsStringify;

  /**
   * 创建抖店请求数据内容创建器
   *
   * @param paramsStringify 抖店请求参数字符串序列化器
   */
  public DoudianContentCreatorImpl(DoudianParamsStringify paramsStringify) {
    Asserts.notNull(paramsStringify, "paramsStringify");
    this.paramsStringify = paramsStringify;
  }

  /**
   * 创建抖店请求数据内容创建器，使用默认的抖店请求参数字符串序列化器
   *
   * @see DoudianParamsStringifyJsonImpl
   */
  public DoudianContentCreatorImpl() {
    this(DoudianParamsStringifyJsonImpl.create());
  }

  @Override
  public <D> DoudianContent<D> create(DoudianParams params, AppConfig appConfig) {
    Asserts.notNull(params, "params");
    Asserts.notNull(appConfig, "appConfig");

    String timestamp = TimestampUtils.getCurrentTimestamp();
    String paramsString = paramsStringify.parseString(params, true);
    String version = "2";

    String input = "app_key" + appConfig.getAppKey() +
            "method" + params.getMethod() + "param_json" + paramsString +
            "timestamp" + timestamp + "v" + version;
    String sign = DigestUtils.md5Hex(appConfig.getAppSecret() + input + appConfig.getAppSecret());

    return DoudianContent.<D>builder()
            .setMethod(params.getMethod()).setAppKey(appConfig.getAppKey())
            .setParamJson(paramsString)
            .setTimestamp(timestamp).setV(version)
            .setSign(sign)
            .build();
  }
}
