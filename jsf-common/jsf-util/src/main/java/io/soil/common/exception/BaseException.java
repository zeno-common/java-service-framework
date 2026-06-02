package io.soil.common.exception;



import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Collections;

/**
 * 异常基类。
 * <p>
 * 主要以下用途
 * 1. 用于制定通用的异常信息，给派生类的构建函数定义进行参考。
 * 2. 有助于构异异常类的构建，并提供多种场景下异常类构建函数的定义参考，
 * 方便快速定义异常类和易于实例化异常类对象。
 *
 * @author wangzezhou
 */
@Slf4j
public abstract class BaseException extends RuntimeException {

	protected BaseException(String msg) {
		this(msg, Collections.emptyList());
	}

	protected BaseException( String msgPattern, Object... msgArgs ){
		this(null, msgPattern, msgArgs);
	}


	protected BaseException( Throwable throwable, String msg ){
		this(throwable, msg, Collections.emptyList());
	}

	protected BaseException( Throwable throwable ){
		this(throwable, throwable.getMessage(), Collections.emptyList());
	}

	protected BaseException( Throwable throwable, String msgPattern, Object... msgArgs ){
		super(MessageFormat.format(msgPattern, msgArgs), throwable);
	}

	/**
	 * 获取异常模块名称
	 *
	 * @return 模块名
	 */
	protected abstract String module();

	/**
	 * 获取异常栈字符串
	 *
	 * @return 异常栈字符串
	 */
	public String getStackTraceString(){
		return getStackTraceString(this);
	}

	/**
	 * 获取异常栈字符串
	 *
	 * @return 异常栈字符串
	 */
	public static String getStackTraceString( Throwable throwable ){

		if( !log.isDebugEnabled() ){
			return "";
		}

		final StringWriter sw = new StringWriter(7680);
		final PrintWriter pw = new PrintWriter(sw, true);

		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
