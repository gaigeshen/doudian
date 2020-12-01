package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.content.Content;

/**
 * @author gaigeshen
 */
public class NoAccessTokenException extends ExecutionException {
  public NoAccessTokenException(String message) {
    super(message);
  }
  public NoAccessTokenException(String message, Throwable cause) {
    super(message, cause);
  }
  public NoAccessTokenException(Throwable cause) {
    super(cause);
  }

  @Override
  public NoAccessTokenException setShopId(String shopId) {
    super.setShopId(shopId);
    return this;
  }

  @Override
  public NoAccessTokenException setContent(Content<?> content) {
    super.setContent(content);
    return this;
  }
}
