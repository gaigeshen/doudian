package me.gaigeshen.doudian.authorization;

import me.gaigeshen.doudian.util.Asserts;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 访问令牌存储器实现，访问令牌将被编码为字符串的形式
 *
 * @author gaigeshen
 */
public class AccessTokenStoreJedisImpl implements AccessTokenStore {

  private static final String REDIS_KEY_PREFIX = "dou_access_token:";

  private static final String REDIS_VALUE_TEMPLATE = "access_token:%s,refresh_token:%s,scope:%s,shopId:%s,shopName:%s,expiresIn:%s,expiresTimestamp:%s,updateTime:%s";

  private final Jedis jedis;

  public AccessTokenStoreJedisImpl(Jedis jedis) {
    this.jedis = Asserts.notNull(jedis, "jedis");
  }

  @Override
  public boolean save(AccessToken accessToken) throws AccessTokenStoreException {
    Asserts.notNull(accessToken, "accessToken");
    String oldValue;
    try {
      oldValue = jedis.getSet(getAccessTokenRedisKey(accessToken.getShopId()), encodeAccessToken(accessToken));
    } catch (Exception e) {
      throw new AccessTokenStoreException("Could not save access token, shop id is " + accessToken.getShopId()
              + ", shop name is " + accessToken.getShopName(), e);
    }
    return Objects.isNull(oldValue);
  }

  @Override
  public void deleteByShopId(String shopId) throws AccessTokenStoreException {
    Asserts.notNull(shopId, "shopId");
    try {
      jedis.del(getAccessTokenRedisKey(shopId));
    } catch (Exception e) {
      throw new AccessTokenStoreException("Could not delete access token, shop id is " + shopId, e);
    }
  }

  @Override
  public AccessToken findByShopId(String shopId) throws AccessTokenStoreException {
    Asserts.notNull(shopId, "shopId");
    try {
      String value = jedis.get(getAccessTokenRedisKey(shopId));
      if (Objects.nonNull(value)) {
        return decodeAccessToken(value);
      }
    } catch (Exception e) {
      throw new AccessTokenStoreException("Could not find access token, shop id is " + shopId, e);
    }
    return null;
  }

  @Override
  public List<AccessToken> findAll() throws AccessTokenStoreException {
    List<AccessToken> allAccessTokens = new ArrayList<>();
    try {
      ScanResult<String> scanResult = jedis.scan(ScanParams.SCAN_POINTER_START, new ScanParams().match(REDIS_KEY_PREFIX + "*").count(10));
      List<String> result = scanResult.getResult();
      if (Objects.nonNull(result)) {
        for (String value : result) {
          if (Objects.nonNull(value)) {
            allAccessTokens.add(decodeAccessToken(value));
          }
        }
      }
      while (!scanResult.isCompleteIteration()) {
        scanResult = jedis.scan(scanResult.getCursor(), new ScanParams().match(REDIS_KEY_PREFIX + "*").count(10));
        result = scanResult.getResult();
        if (Objects.nonNull(result)) {
          for (String value : result) {
            if (Objects.nonNull(value)) {
              allAccessTokens.add(decodeAccessToken(value));
            }
          }
        }
      }
    } catch (Exception e) {
      throw new AccessTokenStoreException("Could not find all access tokens", e);
    }
    return allAccessTokens;
  }

  private String encodeAccessToken(AccessToken accessToken) {
    return String.format(REDIS_VALUE_TEMPLATE,
            accessToken.getAccessToken(), accessToken.getRefreshToken(),
            accessToken.getScope(), accessToken.getShopId(), accessToken.getShopName(),
            accessToken.getExpiresIn(), accessToken.getExpiresTimestamp(),
            accessToken.getUpdateTime().getTime());
  }

  private AccessToken decodeAccessToken(String redisValue) {
    String[] fields = redisValue.split(",");
    return AccessToken.builder()
            .setAccessToken(fields[0]).setRefreshToken(fields[1])
            .setScope(fields[2]).setShopId(fields[3]).setShopName(fields[4])
            .setExpiresIn(Long.parseLong(fields[5])).setExpiresTimestamp(Long.parseLong(fields[6]))
            .setUpdateTime(new Date(Long.parseLong(fields[7])))
            .build();
  }

  private String getAccessTokenRedisKey(String shopId) {
    return REDIS_KEY_PREFIX + shopId;
  }
}
