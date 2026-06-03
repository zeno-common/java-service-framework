package io.soil.util.jdbc;

import java.util.Objects;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * JSF 分页工具类，基于 REST API 规约的 URL 参数进行 PageHelper 分页处理。
 * <p>
 * 支持页码分页（pageNo/pageSize）和偏移量分页（offset/limit）两种模式。
 * </p>
 *
 * @author zeno.w
 */
public class JsfPagingUtil {


  /**
   * 根据 REST API 规约的 URL 参数进行分页处理（默认查询总数）
   *
   * @param <T> 分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> pageByUrlParam(){
    return pageByUrlParam(true);
  }

  /**
   * 根据 REST API 规约的 URL 参数进行分页处理，指定排序字段（默认查询总数）
   *
   * @param order 排序字段
   * @param <T>   分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> pageByUrlParam(String order){
    return pageByUrlParam(order,true);
  }

  /**
   * 根据 REST API 规约的 URL 参数进行分页处理，指定是否查询总数
   *
   * @param count 是否查询总数
   * @param <T>   分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> pageByUrlParam(Boolean count){
    return pageByUrlParam(null,count);
  }

  /**
   * 根据 REST API 规约的 URL 参数进行分页处理，指定排序和是否查询总数。
   * <p>
   * 优先使用 offset/limit 偏移量分页，若无则使用 pageNo/pageSize 页码分页。
   * </p>
   *
   * @param order 排序字段，为 null 时从 URL 参数获取
   * @param count 是否查询总数
   * @param <T>   分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> pageByUrlParam(String order, boolean count){

    // 需要设置 defaultValue 为 null 走到 pageNo 参数逻辑
    Integer offset = JsfUrlParameter.offset(null);
    Integer limit = JsfUrlParameter.limit(null);
    if(offset != null && limit != null){
      return offsetByUrlParams(offset, limit, order);
    }

    Integer pageNo = JsfUrlParameter.pageNo();
    Integer pageSize = JsfUrlParameter.pageSize();
    return pageByUrlParam(pageNo, pageSize, order,count);
  }

  /**
   * 根据 REST API 规约的 URL 参数进行分页处理，指定页码、每页条数和排序（默认查询总数）
   *
   * @param pageNo   当前页码
   * @param pageSize 每页条数
   * @param order    排序字段
   * @param <T>      分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order){
    return pageByUrlParam(pageNo,pageSize,order,true);
  }


  /**
   * 根据 REST API 规约的 URL 参数进行分页处理，指定页码、每页条数、排序和是否查询总数
   *
   * @param pageNo   当前页码
   * @param pageSize 每页条数
   * @param order    排序字段，为 null 时从 URL 参数获取
   * @param count    是否查询总数
   * @param <T>      分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order, boolean count){

    Page<T> page = PageHelper.startPage(pageNo, pageSize, count, true, false);
    if (order == null){
      order = JsfUrlParameter.order();
    }

    if(!Objects.isNull(order)){
      PageHelper.orderBy(order);
    }

    return page;
  }

  /**
   * 使用偏移量进行分页处理（默认查询总数）
   *
   * @param offset 偏移量
   * @param limit  限制条数
   * @param order  排序字段
   * @param <T>    分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order){
    return offsetByUrlParams(offset,limit,order,true);
  }

  /**
   * 使用偏移量进行分页处理
   *
   * @param offset 偏移量
   * @param limit  限制条数
   * @param order  排序字段，为 null 时从 URL 参数获取
   * @param count  是否查询总数
   * @param <T>    分页数据类型
   * @return PageHelper 的 Page 对象
   */
  public static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order,boolean count){

    Page<T> page = PageHelper.offsetPage(offset, limit, count);
    if (order == null){
      order = JsfUrlParameter.order();
    }

    if(!Objects.isNull(order)){
      PageHelper.orderBy(order);
    }

    return page;
  }
}
