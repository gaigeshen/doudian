package me.gaigeshen.doudian.authorization;

/**
 * 访问令牌管理器，维护所有访问令牌的更新
 *
 * @author gaigeshen
 */
public interface AccessTokenManager {

  /**
   * 添加新的访问令牌，如果该访问令牌所属的店铺编号已经存在访问令牌，则会将此新的访问令牌替换掉旧的
   *
   * @param accessToken 待添加的访问令牌不能为空
   * @throws AccessTokenManagerException 无法添加访问令牌
   */
  void addNewAccessToken(AccessToken accessToken) throws AccessTokenManagerException;

  /**
   * 删除访问令牌
   *
   * @param shopId 访问令牌所属店铺编号不能为空
   * @throws AccessTokenManagerException 无法删除访问令牌
   */
  void deleteAccessToken(String shopId) throws AccessTokenManagerException;

  /**
   * 查询访问令牌
   *
   * @param shopId 访问令牌所属店铺编号不能为空
   * @return 访问令牌可能为空
   * @throws AccessTokenManagerException 无法查询访问令牌
   */
  AccessToken findAccessToken(String shopId) throws AccessTokenManagerException;

  /**
   * 关闭此访问令牌管理器
   *
   * @throws AccessTokenManagerException 关闭的时候发生异常
   */
  void shutdown() throws AccessTokenManagerException;
}
