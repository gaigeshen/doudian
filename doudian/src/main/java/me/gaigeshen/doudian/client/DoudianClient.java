package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.config.AppConfig;
import me.gaigeshen.doudian.http.WebClientConfig;

/**
 * @author gaigeshen
 */
public interface DoudianClient {

  void setAppConfig(AppConfig appConfig);

  void setAccessTokenStore(AccessTokenStore accessTokenStore);

  void setWebClientConfig(WebClientConfig webClientConfig);

  void init() throws DoudianClientException;

  void dispose() throws DoudianClientException;
}
