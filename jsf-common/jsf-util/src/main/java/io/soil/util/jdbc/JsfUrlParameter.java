package io.soil.waf.util;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.regex.Pattern;

import static io.soil.util.jdbc.JsfUrlParamConst.*;


public class JsfUrlParameter {

  /** 每页显示条数，默认 10 */
  private static final int DEFAULT_SIZE = 10;

  /** 当前页，默认 1 */
  private static final int DEFAULT_PAGE_NO = 1;

  /** 默认分页最大个数 */
  private static final int PAGE_SIZE_MAX = 500;

  /** 默认偏移量 */
  private static final int DEFAULT_OFFSET = 0;

  /** 默认限制量 */
  private static final int DEFAULT_LIMIT = DEFAULT_SIZE;


  /**
   * 数字正则表达式
   */
  private static final Pattern NUM_PATTERN = Pattern.compile("^[0-9]+$");


  /**
   * 获取请求中的分页大小
   * 数据偏程量
   * @return
   */
  public static Integer offset(Integer defaultOffset) {
    return getInteger(OFFSET, defaultOffset);
  }

  /**
   * 偏移量
   * @return
   */
  public static Integer offset() {
    return getInteger(OFFSET, DEFAULT_OFFSET);
  }

  /**
   * 获取请求中的当前页
   *
   * @return
   */
  public static Integer limit(Integer defaultLimit) {
    return getInteger(LIMIT, defaultLimit);
  }

  public static Integer limit() {
    return getInteger(LIMIT, DEFAULT_LIMIT);
  }


  /**
   * 获取请求中的分页大小
   *
   * @return
   */
  public static Integer pageSize(Integer defaultSize) {
    Integer pageSize = getInteger(PAGE_SIZE, defaultSize);
    if(pageSize > PAGE_SIZE_MAX){
      return PAGE_SIZE_MAX;
    }

    return pageSize;
  }

  public static Long getOffsetId() {
    return getLong(OFFSET_ID);
  }

  public static String getOffsetTime() {
    return getString(OFFSET_TIME);
  }

  public static Integer pageSize() {
    return getInteger(PAGE_SIZE, DEFAULT_SIZE);
  }

  /**
   * 获取请求中的当前页
   *
   * @return
   */
  public static Integer pageNo(Integer defaultPageNo) {
    return getInteger(PAGE_NO,defaultPageNo);
  }

  public static Integer pageNo() {
    return getInteger(PAGE_NO,DEFAULT_PAGE_NO);
  }

  public static String order(){
    return getString(ORDER);
  }

  public static Integer getInteger(String paramName, Integer defaultValue) {
    Integer value = getInteger(paramName);
    if(value == null){
      return defaultValue;
    }

    return value;
  }

  private static Integer getInteger(String urlParameter){

    String valueStr = getString(urlParameter);
    if (StringUtils.isBlank(valueStr) || !NUM_PATTERN.matcher(valueStr).matches()) {
      return null;
    }

    return Integer.valueOf(valueStr);
  }


  private static Long getLong(String urlParameter){
    String valueStr = getString(urlParameter);
    if (StringUtils.isBlank(valueStr) || !NUM_PATTERN.matcher(valueStr).matches()) {
      return null;
    }

    return Long.valueOf(valueStr);
  }

  public static String getString(String paramName){
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      HttpServletRequest request = attributes.getRequest();
    return request.getParameter(paramName);
  }
}
