package io.soil.jsf.common.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import lombok.Data;

/**
 * 通用树节点定义，支持泛型的节点ID和类型。
 * <p>
 * 可用于构建任意层级的树形结构，每个节点包含ID、名称、类型、父节点引用及子节点列表。
 * </p>
 *
 * @param <ID>   节点标识类型
 * @param <TYPE> 节点类型类型
 * @author zeno.w
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

  /**
   * 添加子节点，首次添加时自动初始化子节点列表
   *
   * @param node 要添加的子节点
   */
  public void addChild(Node<ID,TYPE> node ){

    if( this.children.isEmpty() ){
      this.children = new ArrayList<>();
    }

    this.children.add(node);
  }

}
