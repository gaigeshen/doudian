package me.gaigeshen.doudian.authorization;

import java.util.List;
import java.util.Objects;

/**
 * 访问令牌存储器
 *
 * @author gaigeshen
 */
public interface AccessTokenStore {
  /**
   * 保存访问令牌，通过返回值确定本次操作的是否为新店铺的访问令牌
   *
   * @param accessToken 访问令牌不能为空
   * @return 是否为新店铺的访问令牌
   * @throws AccessTokenStoreException 无法保存或者更新访问令牌
   */
  boolean save(AccessToken accessToken) throws AccessTokenStoreException;

  /**
   * 删除访问令牌
   *
   * @param shopId 店铺编号不能为空
   * @throws AccessTokenStoreException 无法删除访问令牌
   */
  void deleteByShopId(String shopId) throws AccessTokenStoreException;

  /**
   * 查询访问令牌
   *
   * @param shopId 店铺编号不能为空
   * @return 访问令牌可能为空
   * @throws AccessTokenStoreException 无法查询访问令牌
   */
  AccessToken findByShopId(String shopId) throws AccessTokenStoreException;

  /**
   * 查询访问令牌
   *
   * @param shopId 店铺编号不能为空
   * @param required 返回的访问令牌是否必须，如果是必须的且未查询到访问令牌则会抛出异常
   * @return 访问令牌可能为空，如果返回的令牌不是必须的话
   * @throws AccessTokenStoreException 无法查询访问令牌
   * @throws AccessTokenNotFoundException 未查询到访问令牌，如果返回的访问令牌是必须的话
   */
  default AccessToken findByShopId(String shopId, boolean required) throws AccessTokenStoreException, AccessTokenNotFoundException {
    AccessToken accessToken = findByShopId(shopId);
    if (required && Objects.isNull(accessToken)) {
      throw new AccessTokenNotFoundException("Could not find access token with shop id " + shopId);
    }
    return accessToken;
  }

  /**
   * 查询所有的访问令牌
   *
   * @return 所有的访问令牌
   * @throws AccessTokenStoreException 无法查询所有的访问令牌
   */
  List<AccessToken> findAll() throws AccessTokenStoreException;

}
