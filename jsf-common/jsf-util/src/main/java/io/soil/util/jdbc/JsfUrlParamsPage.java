package io.soil.util.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;

import lombok.Setter;

/**
 * 基于 URL 参数的 MyBatis-Plus 分页对象，实现 {@link IPage} 接口。
 * <p>
 * 从 HTTP 请求的 URL 参数中自动提取分页信息（页码、每页条数、排序等），
 * 支持 pageNo/pageSize 和 offset/limit 两种分页模式。
 * </p>
 *
 * @param <T> 分页数据类型
 * @author koala
 */
public class JsfUrlParamsPage<T> implements IPage<T>{

    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    protected long total = 0;

    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 0;

    /**
     * 排序字段信息
     */
    @Setter
    protected List<OrderItem> orders = new ArrayList<>();

    /**
     * 自动优化 COUNT SQL
     */
    protected boolean optimizeCountSql = true;
    /**
     * 是否进行 count 查询
     */
    protected boolean searchCount = true;
    /**
     * {@link #optimizeJoinOfCountSql()}
     */
    @Setter
    protected boolean optimizeJoinOfCountSql = true;
    /**
     * 单页分页条数限制
     */
    @Setter
    protected Long maxLimit = 500L
    ;
    /**
     * countId
     */
    @Setter
    protected String countId;


    public JsfUrlParamsPage() {
    }
    /**
     * 分页构造函数
     *
     * @param current 当前页
     * @param size    每页显示条数
     */
    public JsfUrlParamsPage(long current, long size) {
        this(current, size, 0);
    }

    public JsfUrlParamsPage(long current, long size, long total) {
        this(current, size, total, true);
    }

    public JsfUrlParamsPage(long current, long size, boolean searchCount) {
        this(current, size, 0, searchCount);
    }

    public JsfUrlParamsPage(long current, long size, long total, boolean searchCount) {
        if (current > 1) {
            this.current = current;
        }
        this.size = size;
        this.total = total;
        this.searchCount = searchCount;
    }

    /**
     * 是否存在上一页
     *
     * @return true / false
     */
    public boolean hasPrevious() {
        return this.current > 1;
    }

    /**
     * 是否存在下一页
     *
     * @return true / false
     */
    public boolean hasNext() {
        return this.current < this.getPages();
    }

    @Override
    public List<T> getRecords() {
        return this.records;
    }

    @Override
    public IPage<T> setRecords(List<T> records) {
        this.records = records;
        return this;
    }

    @Override
    public long getTotal() {
        return this.total;
    }

    @Override
    public IPage<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public long getSize() {
        return this.size;
    }

    @Override
    public IPage<T> setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public long getCurrent() {
        return this.current;
    }

    @Override
    public IPage<T> setCurrent(long current) {
        this.current = current;
        return this;
    }

    @Override
    public String countId() {
        return this.countId;
    }

    @Override
    public Long maxLimit() {
        return this.maxLimit;
    }

    /**
     * 查找 order 中正序排序的字段数组
     *
     * @param filter 过滤器
     * @return 返回正序排列的字段数组
     */
    private String[] mapOrderToArray(Predicate<OrderItem> filter) {
        List<String> columns = new ArrayList<>(orders.size());
        orders.forEach(i -> {
            if (filter.test(i)) {
                columns.add(i.getColumn());
            }
        });
        return columns.toArray(new String[0]);
    }

    /**
     * 移除符合条件的条件
     *
     * @param filter 条件判断
     */
    private void removeOrder(Predicate<OrderItem> filter) {
        for (int i = orders.size() - 1; i >= 0; i--) {
            if (filter.test(orders.get(i))) {
                orders.remove(i);
            }
        }
    }

    /**
     * 添加新的排序条件，构造条件可以使用工厂：{@link OrderItem#descs(String...)} (String)}、{@link OrderItem#ascs(String...)}
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public JsfUrlParamsPage<T> addOrder(OrderItem... items) {
        orders.addAll(Arrays.asList(items));
        return this;
    }

    /**
     * 添加新的排序条件，构造条件可以使用工厂：{@link OrderItem#descs(String...)} (String)}、{@link OrderItem#ascs(String...)}
     *
     * @param items 条件
     * @return 返回分页参数本身
     */
    public JsfUrlParamsPage<T> addOrder(List<OrderItem> items) {
        orders.addAll(items);
        return this;
    }

    @Override
    public List<OrderItem> orders() {
        return this.orders;
    }

    @Override
    public boolean optimizeCountSql() {
        return optimizeCountSql;
    }

    /**
     * 创建分页对象，指定当前页、每页条数、总数和是否查询总数
     *
     * @param current      当前页
     * @param size         每页条数
     * @param total        总数
     * @param searchCount  是否查询总数
     * @param <T>          分页数据类型
     * @return 分页对象
     */
    public static  <T> JsfUrlParamsPage<T> of(long current, long size, long total, boolean searchCount) {
        return new JsfUrlParamsPage<>(current, size, total, searchCount);
    }

    /**
     * 获取请求中的偏移 ID
     *
     * @return 偏移 ID
     */
    public static Long getOffsetId(){
        return JsfUrlParameter.getOffsetId();
    }

    /**
     * 获取请求中的偏移时间
     *
     * @return 偏移时间字符串
     */
    public static String getOffsetTime(){
        return JsfUrlParameter.getOffsetTime();
    }

    @Override
    public boolean optimizeJoinOfCountSql() {
        return optimizeJoinOfCountSql;
    }

    public JsfUrlParamsPage<T> setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
        return this;
    }

    public JsfUrlParamsPage<T> setOptimizeCountSql(boolean optimizeCountSql) {
        this.optimizeCountSql = optimizeCountSql;
        return this;
    }

    @Override
    public long getPages() {
        return IPage.super.getPages();
    }

    /* --------------- 以下为静态构造方式 --------------- */

    /**
     * 创建分页对象，指定当前页和每页条数
     *
     * @param current 当前页
     * @param size    每页条数
     * @param <T>     分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> of(long current, long size) {
        return of(current, size, 0);
    }

    /**
     * 创建分页对象，指定当前页、每页条数和总数
     *
     * @param current 当前页
     * @param size    每页条数
     * @param total   总数
     * @param <T>     分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> of(long current, long size, long total) {
        return of(current, size, total, true);
    }

    /**
     * 创建分页对象，指定当前页、每页条数和是否查询总数
     *
     * @param current      当前页
     * @param size         每页条数
     * @param searchCount  是否查询总数
     * @param <T>          分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> of(long current, long size, boolean searchCount) {
        return of(current, size, 0, searchCount);
    }

    /**
     * 从 URL 参数创建分页对象，指定是否查询总数
     *
     * @param searchCount 是否查询总数
     * @param <T>         分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> of(boolean searchCount){
        Integer size = JsfUrlParameter.pageSize();
        Integer pageNo = JsfUrlParameter.pageNo();
        return of(pageNo, size, searchCount);
    }

    /**
     * 从 URL 参数创建分页对象（默认查询总数）
     *
     * @param <T> 分页数据类型
     * @return 分页对象
     */
    public static <T> JsfUrlParamsPage<T> urlPage(){
        return of(true);
    }


    @Override
    public boolean searchCount() {
        if (total < 0) {
            return false;
        }
        return searchCount;
    }
}
