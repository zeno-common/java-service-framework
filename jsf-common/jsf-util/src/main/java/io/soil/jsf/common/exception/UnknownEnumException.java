package io.soil.jsf.common.exception;

/**
 * 未知的枚举类型异常，当根据状态值无法匹配到对应的枚举时抛出。
 *
 * @author zeno.w
 */
public class UnknownEnumException extends BaseException{

  /**
   * 构造未知枚举异常
   *
   * @param status     无法匹配的状态值
   * @param enumClazz  枚举类对象
   */
  public UnknownEnumException( Object status,Class<? extends Enum> enumClazz){
    super("未知的 {0} 枚举状态:{1}",enumClazz.getName(),status);
  }

  @Override
  public ExceptionType type(){
    return ExceptionType.UNKNOWN;
  }
}
