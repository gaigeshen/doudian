package me.gaigeshen.doudian.client.authorization;

import me.gaigeshen.doudian.request.result.ResultData;

/**
 * 访问令牌抖店请求执行结果数据
 *
 * @author gaigeshen
 */
public class AccessTokenData implements ResultData {

  public String accessToken;

  public String refreshToken;

  public String scope;

  public String shopId;

  public String shopName;

  public Long expiresIn;

}
