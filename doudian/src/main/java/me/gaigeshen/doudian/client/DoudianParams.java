package me.gaigeshen.doudian.client;

import me.gaigeshen.doudian.util.Asserts;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

/**
 * 抖店请求数据内容里面的参数集合，所有的参数默认将按照参数名称排序
 *
 * @author gaigeshen
 */
public class DoudianParams implements Iterable<DoudianParams.Param> {

  private final SortedSet<Param> params = new TreeSet<>();

  private final String method;

  private final String shopId;

  /**
   * 创建抖店请求参数
   *
   * @param method 该抖店请求参数对应的远程服务名称，不能为空
   * @param shopId 店铺编号不能为空
   */
  public DoudianParams(String method, String shopId) {
    Asserts.notBlank(method, "method");
    Asserts.notBlank(shopId, "shopId");
    this.method = method;
    this.shopId = shopId;
  }

  /**
   * 添加参数
   *
   * @param param 参数
   */
  public void addParam(Param param) {
    if (Objects.isNull(param)) {
      throw new IllegalArgumentException("param cannot be null");
    }
    params.add(param);
  }

  /**
   * 移除所有参数
   */
  public void removeAllParams() {
    params.clear();
  }

  /**
   * 获取参数
   *
   * @param name 参数名称
   * @return 参数可能为空
   */
  public Param getParam(String name) {
    for (Param param : params) {
      if (param.getName().equals(name)) {
        return param;
      }
    }
    return null;
  }

  /**
   * 获取参数值
   *
   * @param name 参数名称
   * @return 参数值可能为空
   */
  public Object getParamValue(String name) {
    Param param = getParam(name);
    if (Objects.isNull(param)) {
      return null;
    }
    return param.getValue();
  }

  /**
   * 返回远程服务名称，不为空
   *
   * @return 远程服务名称
   */
  public String getMethod() {
    return method;
  }

  /**
   * 返回店铺编号，不为空
   *
   * @return 店铺编号
   */
  public String getShopId() {
    return shopId;
  }

  @Override
  public Iterator<Param> iterator() {
    return params.iterator();
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(params);
  }

  /**
   * 创建参数
   *
   * @param name 参数名称不能为空
   * @param value 参数值不能为空
   * @return 参数
   */
  public static Param createParam(String name, Object value) {
    return new SimpleParam(name, value);
  }

  /**
   * 参数
   *
   * @author gaigeshen
   */
  public interface Param extends Comparable<Param> {
    /**
     * 返回参数名称
     *
     * @return 参数名称
     */
    String getName();

    /**
     * 返回参数值
     *
     * @return 参数值
     */
    Object getValue();

    /**
     * 默认比较参数名称
     */
    @Override
    default int compareTo(Param o) {
      return getName().compareTo(o.getName());
    }
  }

  /**
   * 参数实现
   *
   * @author gaigeshen
   */
  public static class SimpleParam implements Param {
    private final String name;
    private final Object value;

    public SimpleParam(String name, Object value) {
      if (Objects.isNull(name)) {
        throw new IllegalArgumentException("name cannot be null");
      }
      if (Objects.isNull(value)) {
        throw new IllegalArgumentException("value cannot be null");
      }
      this.name = name;
      this.value = value;
    }

    @Override
    public String getName() {
      return name;
    }
    @Override
    public Object getValue() {
      return value;
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || obj.getClass() != SimpleParam.class) {
        return false;
      }
      SimpleParam other = (SimpleParam) obj;
      return name.equals(other.name);
    }

    @Override
    public String toString() {
      return ToStringBuilder.reflectionToString(this);
    }
  }
}
