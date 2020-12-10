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

  /**
   * 创建抖店请求参数
   *
   * @param method 该抖店请求参数对应的远程服务名称，不能为空
   */
  public DoudianParams(String method) {
    Asserts.notBlank(method, "method");
    this.method = method;
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
   * 添加参数
   *
   * @param name 参数名称
   * @param value 参数值
   */
  public void addParam(String name, Object value) {
    addParam(createParam(name, value));
  }

  /**
   * 添加很多参数
   *
   * @param nameValues 参数的键值对集合
   */
  public void addParams(Map<String, Object> nameValues) {
    if (Objects.isNull(nameValues)) {
      return;
    }
    for (Map.Entry<String, Object> entry : nameValues.entrySet()) {
      addParam(entry.getKey(), entry.getValue());
    }
  }

  /**
   * 返回远程服务名称，不为空
   *
   * @return 远程服务名称
   */
  public String getMethod() {
    return method;
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
