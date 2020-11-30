package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.authorization.AccessTokenStore;
import me.gaigeshen.doudian.config.AppConfig;
import me.gaigeshen.doudian.request.RequestExecutor;

/**
 *
 * @author gaigeshen
 */
public interface DoudianClient extends RequestExecutor {

  AppConfig getAppConfig();

  AccessTokenStore getAccessTokenStore();


}
