package io.soil.common.collection;

@FunctionalInterface
public interface FunctionWith2Param<S, R, P1,P2> {
  R apply(S s, P1 p1,P2 p2);
}
