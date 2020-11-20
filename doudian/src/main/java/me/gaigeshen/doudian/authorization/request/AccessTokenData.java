package me.gaigeshen.doudian.authorization.request;

import lombok.Data;

/**
 * @author gaigeshen
 */
@Data
public class AccessTokenData {

  private String accessToken;

  private String refreshToken;

  private String scope;

  private String shopId;

  private String shopName;

  private Long expiresIn;

}
