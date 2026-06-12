package io.soil.waf.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康探针控制器，提供应用活跃状态检测接口
 *
 * @author zeno
 */
@RestController
@RequestMapping("/v1/probes")
public class ProbeController{

  /**
   * 活跃探针
   */
  @GetMapping("/activeness")
  public String activeness(){
    return "active";
  }
}