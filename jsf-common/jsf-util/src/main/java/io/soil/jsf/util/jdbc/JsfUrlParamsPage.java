package io.soil.jsf.util.jdbc;

import com.mybatisflex.core.paginate.Page;

/**
 * 基于 URL 参数的 MyBatis-Flex 分页对象，继承 {@link Page}。
 * <p>
 * 从 HTTP 请求的 URL 参数中自动提取分页信息（页码、每页条数），
 * 支持 pageNo/pageSize 和 offset/limit 两种分页模式。
 * 排序参数通过 {@link JsfUrlParams#order()} 获取，由调用方应用于 QueryWrapper。
 * </p>
 *
 * @param <T> 分页数据类型
 * @author koala
 */
public class JsfUrlParamsPage<T> extends Page<T> {

    public JsfUrlParamsPage() {
    }

    /**
     * 分页构造函数
     *
     * @param pageNumber 当前页
     * @param pageSize   每页显示条数
     */
    public JsfUrlParamsPage(int pageNumber, int pageSize) {
        super(pageNumber, pageSize);
    }

    /**
     * 分页构造函数
     *
     * @param pageNumber 当前页
     * @param pageSize   每页显示条数
     * @param totalRow   总数
     */
    public JsfUrlParamsPage(int pageNumber, int pageSize, long totalRow) {
        super(pageNumber, pageSize);
        setTotalRow(totalRow);
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return getPageNumber() > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return getPageNumber() < getTotalPage();
    }

    /**
     * 获取请求中的排序参数
     *
     * @return 排序字符串，参数不存在时返回 null
     */
    public String order() {
        return JsfUrlParams.order();
    }

    /* --------------- 以下为静态构造方式 --------------- */

    /**
     * 创建分页对象，指定当前页和每页条数
     *
     * @param pageNumber 当前页
     * @param pageSize   每页显示条数
     * @param <T>        分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> of(long pageNumber, long pageSize) {
        return new JsfUrlParamsPage<>((int) pageNumber, (int) pageSize);
    }

    /**
     * 创建分页对象，指定当前页、每页条数和总数
     *
     * @param pageNumber 当前页
     * @param pageSize   每页显示条数
     * @param totalRow   总数
     * @param <T>        分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> of(long pageNumber, long pageSize, long totalRow) {
        return new JsfUrlParamsPage<>((int) pageNumber, (int) pageSize, totalRow);
    }

    /**
     * 从 URL 参数创建分页对象（默认查询总数）
     *
     * @param <T> 分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> ofUrlParams() {
      int pageSize = JsfUrlParams.pageSize();
      int pageNo = JsfUrlParams.pageNo();
      return new JsfUrlParamsPage<>(pageNo, pageSize);
    }
}
