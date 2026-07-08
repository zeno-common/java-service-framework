package io.soil.jsf.common.exception;

/**
 * 异常类型枚举，用于标识异常的来源分类。
 * <p>
 * 对应 COLA 5 分层架构中的异常分类规则：
 * <ul>
 *   <li>{@link #UNDEFINED} — 未定义异常, 用于表示未分类的异常类型</li>
 *   <li>{@link #BIZ} — 业务异常，Domain/App 层违反业务规则时抛出</li>
 *   <li>{@link #SYS} — 系统异常，Infrastructure 层捕获技术异常后包装抛出</li>
 *   <li>{@link #PARAM} — 参数校验异常，比如 Adapter 层入参校验失败时抛出</li>
 * </ul>
 *
 * @author zeno.w
 */
public enum ExceptionType {

  /** 未定义异常 */
  UNDEFINED,

	/** 业务异常 */
	BIZ,

	/** 系统异常 */
	SYS,

	/** Adapter 层的参数校验异常 */
  PARAM
}
