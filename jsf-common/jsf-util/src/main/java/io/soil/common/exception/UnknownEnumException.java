package io.soil.common.exception;

/**
 * 未知的枚举类型异常
 * @author wangzezhou
 */
public class UnknownEnumException extends BaseException{

  public UnknownEnumException( Object status,Class<? extends Enum> enumClazz){
    super("未知的 {0} 枚举状态:{1}",enumClazz.getName(),status);
  }

  /**
   * 获取异常模块名称
   *
   * @return 模块名
   */
  @Override
  protected String module(){
    return "JSF";
  }
}
