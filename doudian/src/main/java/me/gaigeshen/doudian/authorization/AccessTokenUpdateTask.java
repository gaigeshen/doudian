package me.gaigeshen.doudian.authorization;

/**
 * 任务类，用于更新访问令牌，每个此任务对象应该对应某个访问令牌，根据设置的店铺编号进行区分
 *
 * @author gaigeshen
 * @see Runnable
 */
public interface AccessTokenUpdateTask extends Runnable {
  /**
   * 设置访问令牌存储器到此任务，此存储器用于获取访问令牌和保存更新之后的访问令牌
   *
   * @param accessTokenStore 访问令牌存储器
   */
  void setAccessTokenStore(AccessTokenStore accessTokenStore);

  /**
   * 设置访问令牌更新监听器，监听器应该不是必须设置的
   *
   * @param accessTokenUpdateListener 访问令牌更新监听器
   */
  void setAccessTokenUpdateListener(AccessTokenUpdateListener accessTokenUpdateListener);

  /**
   * 设置店铺编号到此任务，此店铺编号用于在执行更新任务的时候从存储器中查询访问令牌
   *
   * @param shopId 店铺编号
   */
  void setShopId(String shopId);

  /**
   * 执行具体的更新任务
   *
   * @throws AccessTokenUpdateException 更新的时候发生异常
   */
  void executeUpdate() throws AccessTokenUpdateException;

  /**
   * 不需要重新实现此方法，内部直接调用执行具体的更新任务方法，将访问令牌更新异常转换为运行时异常
   */
  @Override
  default void run() {
    try {
      executeUpdate();
    } catch (AccessTokenUpdateException e) {
      throw new IllegalStateException("Could not update access token" + (e.hasCurrentAccessToken() ? " for current access token " + e.getCurrentAccessToken() : ""), e);
    }
  }
}
