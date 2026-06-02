package io.soil.util.jdbc;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.soil.waf.util.JsfUrlParameter;

import java.util.Objects;

/**
 * @author wangzezhou
 */
public class JsfPagingUtil {


  /** 根据 REST api 规约的分页处理 */
  public static <T> Page<T> pageByUrlParam(){
    return pageByUrlParam(true);
  }

  /** 根据 REST api 规约的分页处理 */
  public static <T> Page<T> pageByUrlParam(String order){
    return pageByUrlParam(order,true);
  }

  /** 根据 REST api 规约的分页处理 */
  public static <T> Page<T> pageByUrlParam(Boolean count){
    return pageByUrlParam(null,count);
  }

  /** 根据 REST api 规约的分页处理 */
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

  /** 根据 REST api 规约的分页处理 */
  public static <T> Page<T> pageByUrlParam(Integer pageNo, Integer pageSize, String order){
    return pageByUrlParam(pageNo,pageSize,order,true);
  }


  /** 根据 REST api 规约的分页处理 */
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

  public static <T> Page<T> offsetByUrlParams(Integer offset, Integer limit, String order){
    return offsetByUrlParams(offset,limit,order,true);
  }

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
