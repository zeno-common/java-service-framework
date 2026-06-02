package io.soil.common.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 通用树节点定义
 *
 * @author wangzezhou
 */
@Data
public class Node<ID,TYPE>{

  /** 节点对象标识 */
  private ID id;

  /** 节点对象名称 */
  private String name;

  /** 节点对象类型 */
  private TYPE type;

  /**
   * 父节点标识
   * 为 null 表示顶级节点
   */
  private ID parentId;

  /** 节点序数 */
  private Number ordinal;

  /** 树节点的扩展部分 */
  private Object addition;

  /** 子节点列表 */
  private Collection<Node<ID,TYPE>> children;

  public Node(){
    children = Collections.emptyList();
  }

  public void addChild(Node<ID,TYPE> node ){

    if( this.children.isEmpty() ){
      this.children = new ArrayList<>();
    }

    this.children.add(node);
  }

}
