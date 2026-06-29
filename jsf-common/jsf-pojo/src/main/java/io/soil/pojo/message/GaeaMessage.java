//package com.skylink.pojo.message;
//
//import com.skylink.gaea.leaf.snowflake.GaeaLeafId;
//import lombok.Data;
//
///**
// * 基本消息体模板定义，用于
// *
// * B 消息体定型
// * T 消息类型枚举
// *
// * @author zeno
// */
//@Data
//public abstract class GaeaMessage<T extends Enum>{
//
//  /** 唯一消息ID */
//  private final Long msgId;
//
//  /** 消息类型 */
//  private T type;
//
//  public String topic(){
//    return type.getClass().getSimpleName();
//  }
//
//  public GaeaMessage(T type){
//    this();
//
//    this.type = type;
//  }
//
//  public GaeaMessage(){
//    this.msgId = GaeaLeafId.nextId();
//  }
//}
