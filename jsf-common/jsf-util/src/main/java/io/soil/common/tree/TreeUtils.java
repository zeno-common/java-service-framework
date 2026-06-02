package io.soil.common.tree;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * tree 模型工具类
 * 支持 list、tree、map 等村节点在各种数据结构中的转换。
 *
 * @author wangzezhou
 */
public class TreeUtils{

  /**
   * list 转换成 tree
   *
   * @param nodes list 节点
   *
   * @return tree 结构模型
   */
  public static <ID,TYPE,R extends Node<ID,TYPE>> List<R> toTree( List<R> nodes){

    Map<ID,? extends Node<ID,TYPE>> nodeMap = toMap(nodes);
    Set<? extends Map.Entry<ID,? extends Node<ID,TYPE>>> entries = nodeMap.entrySet();

    List<Node<ID,TYPE>> trees = new ArrayList<>();
    for(Map.Entry<ID,? extends Node<ID,TYPE>> entry: entries){

      Node<ID,TYPE> value = entry.getValue();

      ID parentId = value.getParentId();
      Node<ID,TYPE> parentNode = nodeMap.get(parentId);
      if( parentNode == null ){
        trees.add(value);
      }else{
        parentNode.addChild(value);
      }
    }

    return (List<R>)trees;
  }

  /**
   * 将 tree 转换成 list
   *
   * @param treeNodes
   * @param <ID,TYPE>      节点 ID
   *
   * @return list 结构
   */
  public static <ID,TYPE,R extends Node<ID,TYPE>> List<R> toList( Collection<R> treeNodes ){

    List<Node<ID,TYPE>> list = new ArrayList<>(treeNodes);
    for(int i = 0; i < list.size(); ++i){
      Node<ID,TYPE> node = list.get(i);
      if( node.getChildren() == null || node.getChildren().isEmpty() ){
        continue;
      }

      list.addAll(node.getChildren());
    }

    return (List<R>)list;
  }

  /**
   * 将 list 或 tree 转换成 map
   *
   * @param nodes list 或 tree 节点
   * @param <ID,TYPE>  节点ID
   *
   * @return ID map
   */
  public static <ID,Type,R extends Node<ID,Type>> Map<ID,R> toMap( Collection<R> nodes ){

    List<Node<ID,Type>> list = new ArrayList<>(nodes);

    Map<ID,Node<ID,Type>> treeNodeMap = new HashMap<>(nodes.size());
    for(int i = 0; i < list.size(); ++i){
      Node<ID,Type> node = list.get(i);
      treeNodeMap.put(node.getId(),node);

      if(node.getChildren() == null || node.getChildren().isEmpty() ){
        continue;
      }

      // 将子节点加入到集合尾部，用于继续遍历
      list.addAll(node.getChildren());
    }

    return (Map<ID,R>)treeNodeMap;
  }
}

