package io.soil.common.collection;

@FunctionalInterface
public interface FunctionWith3Param<S, R, P1,P2,P3> {
  R apply(S s, P1 p1,P2 p2,P3 p3);
}
