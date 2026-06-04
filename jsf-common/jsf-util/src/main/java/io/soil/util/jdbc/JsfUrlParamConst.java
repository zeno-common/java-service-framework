package io.soil.util.jdbc;

/**
 * @javadoc 【必须加这个注解，smart-doc才扫描本类所有方法】
 * JSF URL 参数常量类，定义 REST API 规约中分页、排序、偏移等 URL 参数名称。
 *
 * @author zeno.w
 */
public class JsfUrlParamConst {

    /** 大于 ID 的查询参数名，用于轮询 */
    public static final String OFFSET_ID = "$offsetId";

    /** 大于时间的查询参数名，用于轮询 */
    public static final String OFFSET_TIME = "$offsetTime";

    /** 排序参数名，多列排序使用逗号（','）分隔，在列名后面跟随排序顺序（asc 正序，desc 反序），如 name asc,update_time desc */
    public static final String ORDER = "$order";

    /** 分页页码参数名 */
    public static final String PAGE_NO = "$pageNo";

    /** 分页条目数参数名 */
    public static final String PAGE_SIZE = "$pageSize";

    /** 偏移量参数名 */
    public static final String OFFSET = "$offset";

    /** 偏移限制量参数名 */
    public static final String LIMIT = "$limit";
}
