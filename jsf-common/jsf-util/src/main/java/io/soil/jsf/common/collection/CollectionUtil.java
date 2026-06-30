package io.soil.jsf.common.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 集合工具类，提供集合转换、映射等常用操作。
 *
 * @author zeno
 */
public class CollectionUtil{

  /**
   * 将集合转换为 Map，使用指定的 key 映射函数提取键，值保持不变
   *
   * @param values     源集合
   * @param keyMapper  键映射函数
   * @param <KEY>      Map 键类型
   * @param <VALUE>    Map 值类型
   * @return 以 keyMapper 提取的键为键、原元素为值的 Map
   */
  public static <KEY,VALUE> Map<KEY,VALUE> toMap(
    Collection<VALUE> values,Function<VALUE,KEY> keyMapper ){
    return values.stream().collect(Collectors.toMap(keyMapper,Function.identity()));
  }

  /**
   * 将源集合中的元素映射为新的列表
   *
   * @param source 源集合
   * @param mapper 映射函数
   * @param <T>    源元素类型
   * @param <R>    目标元素类型
   * @return 映射后的新列表，源集合为空时返回空列表
   */
  public static <T,R> List<R> mapList(Collection<T> source, Function<T,R> mapper){
    if( source == null || source.isEmpty() ){
      return Collections.emptyList();
    }

    return source.stream().map(mapper).collect(Collectors.toList());
  }


  /**
   * 将源集合中的元素使用带一个额外参数的映射函数映射为新的列表
   *
   * @param source 源集合
   * @param mapper 带一个额外参数的映射函数
   * @param param1 额外参数
   * @param <S>    源元素类型
   * @param <R>    目标元素类型
   * @param <P>    额外参数类型
   * @return 映射后的新列表，源集合为空时返回空列表
   */
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


  /**
   * 将源集合中的元素使用带两个额外参数的映射函数映射为新的列表
   *
   * @param source 源集合
   * @param mapper 带两个额外参数的映射函数
   * @param p1     第一个额外参数
   * @param p2     第二个额外参数
   * @param <S>    源元素类型
   * @param <R>    目标元素类型
   * @param <P1>   第一个额外参数类型
   * @param <P2>   第二个额外参数类型
   * @return 映射后的新列表，源集合为空时返回空列表
   */
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


  /**
   * 将源集合中的元素使用带三个额外参数的映射函数映射为新的列表
   *
   * @param source 源集合
   * @param mapper 带三个额外参数的映射函数
   * @param p1     第一个额外参数
   * @param p2     第二个额外参数
   * @param p3     第三个额外参数
   * @param <S>    源元素类型
   * @param <R>    目标元素类型
   * @param <P1>   第一个额外参数类型
   * @param <P2>   第二个额外参数类型
   * @param <P3>   第三个额外参数类型
   * @return 映射后的新列表，源集合为空时返回空列表
   */
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
