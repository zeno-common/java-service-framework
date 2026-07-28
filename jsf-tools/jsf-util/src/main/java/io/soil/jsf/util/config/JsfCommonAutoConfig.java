package io.soil.jsf.util.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * JSF 通用模块自动配置类，扫描 io.soil.jsf.util 包下的组件。
 * <p>
 * 引入 jsf-util 依赖后，Spring Boot 自动加载此配置类，
 * 扫描 {@code io.soil.jsf.util} 包下所有 {@code @Component}，包括：
 * <ul>
 *   <li>{@link io.soil.jsf.util.spring.ApplicationContextUtil} — Spring 上下文工具</li>
 * </ul>
 * 无需手动配置。
 * </p>
 *
 * @author wangzehou
 */
@Configuration
@ComponentScan({"io.soil.jsf.util"})
public class JsfCommonAutoConfig {
}
