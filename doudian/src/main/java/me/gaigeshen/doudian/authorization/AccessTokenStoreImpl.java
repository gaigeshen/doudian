package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 访问令牌存储器默认的实现，采用简单的哈希存储
 *
 * @author gaigeshen
 */
public class AccessTokenStoreImpl implements AccessTokenStore {

  private final Map<String, AccessToken> internalStore = new ConcurrentHashMap<>();

  @Override
  public boolean save(AccessToken accessToken) {
    Asserts.notNull(accessToken, "accessToken");
    return Objects.isNull(internalStore.put(accessToken.getShopId(), accessToken));
  }

  @Override
  public void deleteByShopId(String shopId) {
    Asserts.notNull(shopId, "shopId");
    internalStore.remove(shopId);
  }

  @Override
  public AccessToken findByShopId(String shopId) {
    Asserts.notNull(shopId, "shopId");
    return internalStore.get(shopId);
  }

  @Override
  public List<AccessToken> findAll() {
    return new ArrayList<>(internalStore.values());
  }
}
