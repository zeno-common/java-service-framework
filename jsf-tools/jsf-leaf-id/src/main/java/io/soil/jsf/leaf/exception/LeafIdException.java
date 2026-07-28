package io.soil.jsf.leaf.exception;


import io.soil.jsf.common.exception.BaseException;
import io.soil.jsf.common.exception.ExceptionType;

/**
 * Leaf ID 生成器异常
 * <p>
 * 封装 ID 生成过程中的各类错误，包括但不限于：
 * <ul>
 *   <li>时钟回拨超过容忍阈值</li>
 *   <li>workerId 超出合法范围</li>
 *   <li>无可用 workerId（所有 worker 均被占用）</li>
 *   <li>乐观锁抢占 worker 失败</li>
 * </ul>
 * </p>
 *
 * @author zeno
 */
public class LeafIdException extends BaseException {

  /**
   * 简单消息构造
   *
   * @param msg 异常描述
   */
  public LeafIdException(String msg ){
    super(msg);
  }

  /**
   * 消息模板构造，支持占位符替换
   * <p>
   * 使用方式：{@code new LeafIdException("worker {0} failed", workerId)}
   * </p>
   *
   * @param msgPattern 消息模板，{0}、{1} 等为占位符
   * @param msgArgs    占位符参数
   */
  public LeafIdException(String msgPattern, Object... msgArgs){
    super(msgPattern,msgArgs);
  }

  /**
   * 包装底层异常
   *
   * @param throwable 底层异常
   * @param msg       异常描述
   */
  public LeafIdException(Throwable throwable, String msg) {
    super("GENERATE-ID", throwable, msg);
  }

  @Override
  public ExceptionType type() {
    return ExceptionType.SYS;
  }
}
