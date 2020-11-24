package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌更新监听器
 *
 * @author gaigeshen
 */
@FunctionalInterface
public interface AccessTokenUpdateListener {
  /**
   * 在访问令牌更新成功之后被执行，此方法不要抛出任何异常
   *
   * @param oldAccessToken 旧的访问令牌不能为空
   * @param newAccessToken 新的访问令牌不能为空
   */
  void handleUpdated(AccessToken oldAccessToken, AccessToken newAccessToken);

  /**
   * 在访问令牌更新失败之后被执行，默认不做任何事情，此方法不要抛出任何异常
   *
   * @param exception 异常不能为空
   */
  default void handleFailed(AccessTokenUpdateException exception) {

  }

}
