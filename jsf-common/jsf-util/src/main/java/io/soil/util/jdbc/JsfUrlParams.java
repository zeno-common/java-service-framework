package io.soil.util.jdbc;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.regex.Pattern;

import static io.soil.util.jdbc.JsfUrlParamConst.*;


/**
 * JSF URL 参数工具类，从 HTTP 请求中提取分页、排序、偏移等 URL 参数。
 * <p>
 * 支持从当前请求上下文中自动获取分页页码、每页条数、偏移量、限制量、排序等参数，
 * 并提供默认值和参数校验。
 * </p>
 *
 * @author zeno.w
 */
public class JsfUrlParams {

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
   * 获取请求中的偏移量
   *
   * @param defaultOffset 默认偏移量，当请求参数不存在时使用
   * @return 偏移量
   */
  public static Integer offset(Integer defaultOffset) {
    return getInteger(OFFSET, defaultOffset);
  }

  /**
   * 获取请求中的偏移量，默认为 0
   *
   * @return 偏移量
   */
  public static Integer offset() {
    return getInteger(OFFSET, DEFAULT_OFFSET);
  }

  /**
   * 获取请求中的限制条数
   *
   * @param defaultLimit 默认限制条数，当请求参数不存在时使用
   * @return 限制条数
   */
  public static Integer limit(Integer defaultLimit) {
    return getInteger(LIMIT, defaultLimit);
  }

  /**
   * 获取请求中的限制条数，默认为 10
   *
   * @return 限制条数
   */
  public static Integer limit() {
    return getInteger(LIMIT, DEFAULT_LIMIT);
  }


  /**
   * 获取请求中的每页条数，超过最大值时自动截断为 500
   *
   * @param defaultSize 默认每页条数，当请求参数不存在时使用
   * @return 每页条数
   */
  public static Integer pageSize(Integer defaultSize) {
    Integer pageSize = getInteger(PAGE_SIZE, defaultSize);
    if(pageSize > PAGE_SIZE_MAX){
      return PAGE_SIZE_MAX;
    }

    return pageSize;
  }

  /**
   * 获取请求中的偏移 ID，用于轮询查询
   *
   * @return 偏移 ID，参数不存在时返回 null
   */
  public static Long getOffsetId() {
    return getLong(OFFSET_ID);
  }

  /**
   * 获取请求中的偏移时间，用于轮询查询
   *
   * @return 偏移时间字符串，参数不存在时返回 null
   */
  public static String getOffsetTime() {
    return getString(OFFSET_TIME);
  }

  /**
   * 获取请求中的每页条数，默认为 10
   *
   * @return 每页条数
   */
  public static Integer pageSize() {
    return getInteger(PAGE_SIZE, DEFAULT_SIZE);
  }

  /**
   * 获取请求中的当前页码
   *
   * @param defaultPageNo 默认页码，当请求参数不存在时使用
   * @return 当前页码
   */
  public static Integer pageNo(Integer defaultPageNo) {
    return getInteger(PAGE_NO,defaultPageNo);
  }

  /**
   * 获取请求中的当前页码，默认为 1
   *
   * @return 当前页码
   */
  public static Integer pageNo() {
    return getInteger(PAGE_NO,DEFAULT_PAGE_NO);
  }

  /**
   * 获取请求中的排序参数
   *
   * @return 排序字符串，参数不存在时返回 null
   */
  public static String order(){
    return getString(ORDER);
  }

  /**
   * 获取请求中指定参数名的整数值，带默认值
   *
   * @param paramName    参数名
   * @param defaultValue 默认值
   * @return 参数值的整数形式，参数不存在时返回默认值
   */
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

  /**
   * 获取请求中指定参数名的字符串值
   *
   * @param paramName 参数名
   * @return 参数值字符串
   */
  public static String getString(String paramName){
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      HttpServletRequest request = attributes.getRequest();
    return request.getParameter(paramName);
  }
}
