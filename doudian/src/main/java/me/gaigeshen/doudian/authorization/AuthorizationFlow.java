package me.gaigeshen.doudian.authorization;

/**
 * 授权流程处理
 *
 * @author gaigeshen
 */
public interface AuthorizationFlow {
  /**
   * 获取授权链接
   *
   * @return 授权链接
   */
  String getAuthorizeUrl();

  /**
   * 处理授权码
   *
   * @param authorizationCode 授权码不能为空，如果为空则应该抛出授权异常
   * @throws AuthorizationException 处理授权码过程发生异常
   */
  void processAuthorizationCode(String authorizationCode) throws AuthorizationException;

}
