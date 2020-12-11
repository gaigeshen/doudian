package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.AbstractContent;
import me.gaigeshen.doudian.request.content.MetadataAttributes;

/**
 * 抖店请求数据内容
 *
 * @author gaigeshen
 */
@MetadataAttributes(
        "https://openapi-fxg.jinritemai.com"
)
public class DoudianContent<D> extends AbstractContent<DoudianResult<D>> {

  private final String method;

  private final String appKey;

  private final String paramJson;

  private final String timestamp;

  private final String v;

  private final String sign;

  private DoudianContent(Builder<D> builder) {
    this.method = builder.method;
    this.appKey = builder.appKey;
    this.paramJson = builder.paramJson;
    this.timestamp = builder.timestamp;
    this.v = builder.v;
    this.sign = builder.sign;
  }

  public static <D> Builder<D> builder() {
    return new Builder<>();
  }

  public String getMethod() {
    return method;
  }

  public String getAppKey() {
    return appKey;
  }

  public String getParamJson() {
    return paramJson;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public String getV() {
    return v;
  }

  public String getSign() {
    return sign;
  }

  /**
   * Content object builder
   *
   * @author gaigeshen
   */
  public static class Builder<D> {

    private String method;

    private String appKey;

    private String paramJson;

    private String timestamp;

    private String v;

    private String sign;

    public Builder<D> setMethod(String method) {
      this.method = method;
      return this;
    }

    public Builder<D> setAppKey(String appKey) {
      this.appKey = appKey;
      return this;
    }

    public Builder<D> setParamJson(String paramJson) {
      this.paramJson = paramJson;
      return this;
    }

    public Builder<D> setTimestamp(String timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public Builder<D> setV(String v) {
      this.v = v;
      return this;
    }

    public Builder<D> setSign(String sign) {
      this.sign = sign;
      return this;
    }

    public DoudianContent<D> build() {
      return new DoudianContent<>(this);
    }
  }
}
