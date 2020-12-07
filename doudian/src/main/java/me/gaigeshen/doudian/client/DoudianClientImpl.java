package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessToken;
import me.gaigeshen.doudian.authorization.AccessTokenNotFoundException;
import me.gaigeshen.doudian.authorization.AccessTokenStoreException;
import me.gaigeshen.doudian.util.Asserts;

/**
 * 抖店客户端实现，使用此类对象之前，确保先调用了初始化方法，初始化正常才可继续使用，建议在系统初始化的时候调用此类对象的初始化方法
 *
 * @author gaigeshen
 */
public class DoudianClientImpl extends AbstractDoudianClient {

  private final DoudianContentCreator contentCreator;

  private DoudianClientImpl(DoudianContentCreator contentCreator) {
    this.contentCreator = Asserts.notNull(contentCreator, "contentCreator");
  }

  /**
   * 创建抖店客户端
   *
   * @param contentCreator 用于创建抖店请求数据内容
   * @return 抖店客户端
   */
  public static DoudianClientImpl create(DoudianContentCreator contentCreator) {
    return new DoudianClientImpl(contentCreator);
  }

  /**
   * 创建抖店客户端，使用默认的抖店请求数据内容创建器
   *
   * @return 抖店客户端
   * @see DoudianContentCreatorImpl
   */
  public static DoudianClientImpl create() {
    return create(new DoudianContentCreatorImpl());
  }

  /**
   * 执行抖店远程服务请求
   *
   * @param params 抖店请求参数
   * @param shopId 店铺编号，不可为空
   * @param <D> 抖店请求执行结果中数据部分的类型
   * @return 抖店请求执行结果中数据
   * @throws ExecutionException 请求执行异常
   * @throws ExecutionResultException 请求执行结果异常，可以认为请求执行业务结果是失败的
   * @throws NoAccessTokenException 该店铺不存在访问令牌
   */
  public <D> D execute(DoudianParams params, String shopId) throws ExecutionException {
    DoudianContent<D> content = contentCreator.create(params, getAppConfig());
    AccessToken accessToken;
    try {
      accessToken = getAccessTokenStore().findByShopId(shopId, true);
    } catch (AccessTokenStoreException e) {
      throw new ExecutionException(e).setContent(content);
    } catch (AccessTokenNotFoundException e) {
      throw new NoAccessTokenException(e).setContent(content);
    }
    return execute(content, accessToken.getAccessToken());
  }
}
