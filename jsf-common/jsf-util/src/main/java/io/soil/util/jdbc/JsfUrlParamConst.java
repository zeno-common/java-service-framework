package io.soil.util.jdbc;

/**
 * 盖亚常量
 */
public class JsfUrlParamConst {

    /**
     * 大于 ID 的查询，用于轮
     */
    public static final String OFFSET_ID = "$offsetId";

    /**
     * 大于 ID 的查询，用于轮
     */
    public static final String OFFSET_TIME = "$offsetTime";

    /** 排序，多列排序使用逗号（','）分隔，在列名后面跟随排序顺序（asc 正序， desc 反序），如 name asc,update_time desc */
    public static final String ORDER = "$order";

    /** 分页页码 */
    public static final String PAGE_NO = "$pageNo";

    /**
     * 分页条目数
     */
    public static final String PAGE_SIZE = "$pageSize";

    /** 偏移量 */
    public static final String OFFSET = "$offset";

    /** 偏移限制量 */
    public static final String LIMIT = "$limit";
}
