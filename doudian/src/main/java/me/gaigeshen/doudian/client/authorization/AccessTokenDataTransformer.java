package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.client.DoudianDataTransformer;
import me.gaigeshen.doudian.client.DoudianDataTransformerException;

/**
 * 用于将访问令牌数据转换成访问令牌对象
 *
 * @author gaigeshen
 */
public class AccessTokenDataTransformer implements DoudianDataTransformer<AccessTokenData, AccessToken> {

  private static final AccessTokenDataTransformer INSTANCE = new AccessTokenDataTransformer();

  /**
   * 返回当前类型的全局实例
   *
   * @return 当前类型的全局实例
   */
  public static AccessTokenDataTransformer getInstance() {
    return INSTANCE;
  }

  @Override
  public AccessToken transform(AccessTokenData data) throws DoudianDataTransformerException {
    if (!AccessTokenDataHelper.isValid(data)) {
      throw new DoudianDataTransformerException("Could not transform to access token because data is invalid").setData(data);
    }
    return AccessTokenDataHelper.transform(data);
  }
}
