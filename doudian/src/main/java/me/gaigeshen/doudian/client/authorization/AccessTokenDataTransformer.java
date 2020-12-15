package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.request.result.ResultDataTransformer;
import me.gaigeshen.doudian.request.result.ResultDataTransformerException;

/**
 * 用于将访问令牌数据转换成访问令牌对象
 *
 * @author gaigeshen
 */
public class AccessTokenDataTransformer implements ResultDataTransformer<AccessTokenData, AccessToken> {

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
  public AccessToken transform(AccessTokenData data) throws ResultDataTransformerException {
    if (!AccessTokenDataHelper.isValid(data)) {
      throw new ResultDataTransformerException("Could not transform to access token because data is invalid")
              .setResultData(data).setTargetClass(AccessToken.class);
    }
    return AccessTokenDataHelper.transform(data);
  }
}
