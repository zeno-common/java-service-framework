package io.soil.jsf.common.constant;


import io.soil.jsf.common.exception.UnknownEnumException;

/**
 * 枚举事件
 * 主要用于枚举定义的参考事例代码。
 *
 * @author zeno.w
 */
public enum EnumSample {

	/** 枚举定义事例 */
	SAMPLE( 0);

	EnumSample(int status) {
		this.status = status;
	}

	/** 枚举状态值 */
	private final int status;

	/**
	 * 获取枚举状态值
	 *
	 * @return 枚举状态值
	 */
	public int status() {
		return status;
	}

	/**
	 * 根据 状态值 获取枚举对象
	 *
	 * @param status 状态值
	 *
	 * @return 枚举对象
	 */
	public EnumSample of(int status) {

		for (EnumSample e : EnumSample.values()) {
			if (e.status == status) {
				return e;
			}
		}

		throw new UnknownEnumException(status, EnumSample.class);
	}
}
