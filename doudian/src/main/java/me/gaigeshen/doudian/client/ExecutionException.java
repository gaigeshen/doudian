package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;

import java.util.Objects;

/**
 * @author gaigeshen
 */
public class ExecutionException extends DoudianClientException {

  private String shopId;

  private Content<?> content;

  public ExecutionException(String message) {
    super(message);
  }

  public ExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutionException(Throwable cause) {
    super(cause);
  }

  public ExecutionException setShopId(String shopId) {
    this.shopId = shopId;
    return this;
  }

  public ExecutionException setContent(Content<?> content) {
    this.content = content;
    return this;
  }

  public String getShopId() {
    return shopId;
  }

  public Content<?> getContent() {
    return content;
  }

  public boolean hasShopId() {
    return Objects.nonNull(shopId);
  }

  public boolean hasContent() {
    return Objects.nonNull(content);
  }
}
