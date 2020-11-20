package me.gaigeshen.doudian.request.content;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Content metadata
 *
 * @author gaigeshen
 */
public class Metadata {
  /**
   * Content type
   */
  public enum Type {
    /**
     * No content data
     */
    NONE,
    /**
     * Content type is json
     */
    JSON,
    /**
     * Content type is parameters
     */
    PARAMETERS,
    /**
     * Content type is multipart parameters
     */
    MULTIPART_PARAMETERS
  }

  private final String url;

  private final String method;

  private final boolean requireAccessToken;

  private final Type type;

  private Metadata(Builder builder) {
    this.url = builder.url;
    this.method = builder.method;
    this.requireAccessToken = builder.requireAccessToken;
    this.type = builder.type;
  }

  public static Builder create() {
    return new Builder();
  }

  /**
   * Returns url, this url may be include url parameters
   *
   * @return The url
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns method, only support 'get' and 'post'
   *
   * @return The method
   */
  public String getMethod() {
    return method;
  }

  /**
   * Returns boolean value
   *
   * @return If require access token, returns {@code true}, this access token will be append to url
   */
  public boolean isRequireAccessToken() {
    return requireAccessToken;
  }

  /**
   * Returns content metadata type
   *
   * @return The type
   */
  public Type getType() {
    return type;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  /**
   * Content metadata builder
   *
   * @author gaigeshen
   */
  public static class Builder {

    private String url;

    private String method;

    private boolean requireAccessToken;

    private Type type;

    public Builder setUrl(String url) {
      this.url = url;
      return this;
    }

    public Builder setMethod(String method) {
      this.method = method;
      return this;
    }

    public Builder setRequireAccessToken(boolean requireAccessToken) {
      this.requireAccessToken = requireAccessToken;
      return this;
    }

    public Builder setType(Type type) {
      this.type = type;
      return this;
    }

    public Metadata build() {
      return new Metadata(this);
    }
  }
}
