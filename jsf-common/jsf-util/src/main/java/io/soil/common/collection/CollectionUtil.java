package io.soil.common.collection;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author zeno
 */
public class CollectionUtil{

  public static <KEY,VALUE> Map<KEY,VALUE> toMap(
    Collection<VALUE> values,Function<VALUE,KEY> keyMapper ){
    return values.stream().collect(Collectors.toMap(keyMapper,Function.identity()));
  }

  public static <T,R> List<R> mapList(Collection<T> source, Function<T,R> mapper){
    if( source == null || source.isEmpty() ){
      return Collections.emptyList();
    }

    return source.stream().map(mapper).collect(Collectors.toList());
  }


  public static <S,R,P> List<R> mapList(Collection<S> source, FunctionWithParam<S,R,P> mapper,P param1){
    if( source == null || source.isEmpty() ){
      return Collections.emptyList();
    }

    List<R> returns = new ArrayList<>(source.size());
    for( S s : source ){
      returns.add(mapper.apply(s,param1));
    }

    return returns;
  }


  public static <S,R,P1,P2> List<R> mapList(Collection<S> source, FunctionWith2Param<S,R,P1,P2> mapper,P1 p1,P2 p2){
    if( source == null || source.isEmpty() ){
      return Collections.emptyList();
    }

    List<R> returns = new ArrayList<>(source.size());
    for( S s : source ){
      returns.add(mapper.apply(s,p1,p2));
    }

    return returns;
  }


  public static <S,R,P1,P2,P3> List<R> mapList(Collection<S> source, FunctionWith3Param<S,R,P1,P2,P3> mapper,P1 p1,P2 p2,P3 p3){
    if( source == null || source.isEmpty() ){
      return Collections.emptyList();
    }

    List<R> returns = new ArrayList<>(source.size());
    for( S s : source ){
      returns.add(mapper.apply(s,p1,p2,p3));
    }

    return returns;
  }
}
