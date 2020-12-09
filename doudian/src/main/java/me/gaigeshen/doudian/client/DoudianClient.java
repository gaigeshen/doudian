package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.request.RequestExecutor;

/**
 *
 * @author gaigeshen
 */
public interface DoudianClient extends RequestExecutor {

  <D extends DoudianData> D executeForData(DoudianParams params, String shopId) throws DoudianClientException;

  void init() throws DoudianClientException;
}
