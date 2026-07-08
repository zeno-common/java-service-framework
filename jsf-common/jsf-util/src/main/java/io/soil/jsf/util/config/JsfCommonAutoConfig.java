package io.soil.jsf.util.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * JSF 通用模块自动配置类，扫描 io.soil.util 包下的组件。
 *
 * @author wangzehou
 */
@Configuration
@ComponentScan({"io.soil.jsf.util"})
public class JsfCommonAutoConfig {
}
