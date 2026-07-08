package io.soil.jsf.common.exception;



import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Collections;

/**
 * 业务异常基类，所有业务、系统、其他级异常的统一抽象父类。
 * <p>
 * 制定异常规约：每个业务从本类派生并实现 {@link #type()} 方法，返回异常类型名称，
 * 以便在日志和监控中快速定位异常来源。
 * <p>
 * 消息格式化：异常消息支持 {@link MessageFormat} 模板语法，
 * 使用 {@code {0}}、{@code {1}} 等占位符进行参数替换，例如：
 * <pre>{@code
 *   new MyException("用户 {0} 不存在", userId)
 * }</pre>
 * <p>
 * 异常栈输出：{@link #getStackTraceString()} 方法仅在 DEBUG 日志级别下返回完整异常栈，
 * 非 DEBUG 级别返回空字符串，避免生产环境输出冗余信息。
 *
 * @author zeno.w
 * @see MessageFormat
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
	 * 获取异常类型名称， 类常类型可以是业务类型、系统类型、其他类型等。
	 *
	 * @return 异常类型
	 */
	protected abstract String type();

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
