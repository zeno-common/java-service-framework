package com.skylink.pojo.dto;

import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * 分页结果响应体
 * @author wangzezhou
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
   * 直接返回空结果
   * @return
   */
  public static <T> PagedResult<T> empty(){
    return new PagedResult<>();
  }

  /**
   * 直接返回空结果
   * @return
   */
  public static <T> PagedResult<T> empty(long total){
    return of(total,Collections.emptyList());
  }

  public static <T> PagedResult<T> of(long total,Collection<T> items){
    return of((int)total,items);
  }

  /**
   * 根据集合列表返回分页结果
   * @param total 总数
   * @param items 分页列表
   * @return
   * @param <T>
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