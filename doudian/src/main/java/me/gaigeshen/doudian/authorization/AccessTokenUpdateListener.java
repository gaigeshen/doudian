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
   * @param oldAccessToken 旧的访问令牌
   * @param newAccessToken 新的访问令牌
   */
  void handleUpdated(AccessToken oldAccessToken, AccessToken newAccessToken);

}
