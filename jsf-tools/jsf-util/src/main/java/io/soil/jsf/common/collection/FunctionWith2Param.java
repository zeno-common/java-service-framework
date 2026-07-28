package io.soil.jsf.common.collection;

/**
 * 带两个额外参数的函数式接口，扩展了标准 Function 以支持两个额外参数的传递。
 *
 * @param <S>  源类型
 * @param <R>  返回类型
 * @param <P1> 第一个额外参数类型
 * @param <P2> 第二个额外参数类型
 */
@FunctionalInterface
public interface FunctionWith2Param<S, R, P1,P2> {
  /**
   * 应用函数
   *
   * @param s  源对象
   * @param p1 第一个额外参数
   * @param p2 第二个额外参数
   * @return 函数执行结果
   */
  R apply(S s, P1 p1,P2 p2);
}
