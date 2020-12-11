package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.client.DoudianClient;
import me.gaigeshen.doudian.client.DoudianExecutionException;
import me.gaigeshen.doudian.client.DoudianExecutionResultException;
import me.gaigeshen.doudian.request.RequestExecutionException;
import me.gaigeshen.doudian.request.RequestExecutionResultException;
import me.gaigeshen.doudian.request.RequestExecutor;
import me.gaigeshen.doudian.request.content.Content;
import me.gaigeshen.doudian.request.result.AbstractResult;

/**
 * 抖店远程服务请求过程用于获取访问令牌数据
 *
 * @author gaigeshen
 */
public class AccessTokenDataExecution implements DoudianClient.Execution<AccessTokenData> {

  private final Content<? extends AbstractResult<AccessTokenData>> content;

  private final Object[] args;

  public AccessTokenDataExecution(Content<? extends AbstractResult<AccessTokenData>> content, Object[] args) {
    this.content = content;
    this.args = args;
  }

  @Override
  public AccessTokenData execute(RequestExecutor executor) throws DoudianExecutionException {
    try {
      return executor.executeForData(content, args);
    } catch (RequestExecutionException e) {
      if (e instanceof RequestExecutionResultException) {
        throw new DoudianExecutionResultException(e).setContent(e.getContent()).setResult(((RequestExecutionResultException) e).getResult());
      }
      throw new DoudianExecutionException(e).setContent(e.getContent());
    }
  }
}
