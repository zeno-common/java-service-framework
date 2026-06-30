package io.soil.jsf.jsf.pojo.dto;

import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 分页结果响应体，用于封装分页查询的返回结果。
 * <p>
 * 包含查询总数和当前页的数据列表，支持泛型以适配不同类型的分页数据。
 * </p>
 *
 * @param <T> 分页数据的元素类型
 * @author zeno.w
 */
@Data
public class PagedResult<T>{

  /** 查询总数 */
  private int total;

  /** 返回项列表 */
  private Collection<T> items;

  private PagedResult(){
    this.items = Collections.emptyList();
    this.total = 0;
  }

  private PagedResult(int total){
    this.items = Collections.emptyList();
    this.total = total;
  }

  /**
   * 直接返回空结果，总数为0，列表为空
   *
   * @param <T> 分页数据的元素类型
   * @return 空的分页结果对象
   */
  public static <T> PagedResult<T> empty(){
    return new PagedResult<>();
  }

  /**
   * 直接返回空结果，列表为空但指定总数
   *
   * @param total 查询总数
   * @param <T> 分页数据的元素类型
   * @return 空列表但指定总数的分页结果对象
   */
  public static <T> PagedResult<T> empty(long total){
    return of(total,Collections.emptyList());
  }

  /**
   * 根据总数和集合列表构建分页结果
   *
   * @param total 总数
   * @param items 分页列表
   * @param <T> 分页数据的元素类型
   * @return 分页结果对象
   */
  public static <T> PagedResult<T> of(long total,Collection<T> items){
    return of((int)total,items);
  }

  /**
   * 根据集合列表返回分页结果
   *
   * @param total 总数
   * @param items 分页列表，为null时自动替换为空列表
   * @param <T> 分页数据的元素类型
   * @return 分页结果对象
   */
  public static <T> PagedResult<T> of(int total,Collection<T> items ){

    PagedResult<T> result = new PagedResult<>();
    result.total = total;
    if( Objects.isNull(items) ){
      result.items = Collections.emptyList();
    }else{
      result.items = items;
    }
    return result;
  }
}
