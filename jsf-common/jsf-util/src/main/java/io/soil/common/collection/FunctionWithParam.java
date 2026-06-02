package io.soil.common.collection;

@FunctionalInterface
public interface FunctionWithParam<S, R, P> {
  R apply(S s, P p);
}
