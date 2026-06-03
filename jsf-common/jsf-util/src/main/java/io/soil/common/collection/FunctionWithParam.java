package io.soil.common.collection;

/**
 * 带一个额外参数的函数式接口，扩展了标准 Function 以支持额外的参数传递。
 *
 * @param <S> 源类型
 * @param <R> 返回类型
 * @param <P> 额外参数类型
 */
@FunctionalInterface
public interface FunctionWithParam<S, R, P> {
  /**
   * 应用函数
   *
   * @param s 源对象
   * @param p 额外参数
   * @return 函数执行结果
   */
  R apply(S s, P p);
}
