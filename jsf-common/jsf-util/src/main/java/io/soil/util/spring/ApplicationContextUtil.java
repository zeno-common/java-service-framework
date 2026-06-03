package io.soil.util.spring;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring ApplicationContext 工具类，提供在非 Spring 管理的类中获取 Bean 的能力。
 *
 * @author zeno
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }


    /**
     * 根据名称获取 Bean 实例
     *
     * @param name Bean 的注册名称
     * @param <T>  Bean 的类型
     * @return 以所给名字注册的 Bean 实例
     * @throws org.springframework.beans.BeansException 如果获取失败
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) context.getBean(name);
    }

    /**
     * 根据类型获取 Bean 实例
     *
     * @param clazz Bean 的类型
     * @param <T>   Bean 的类型
     * @return 对应类型的 Bean 实例
     * @throws org.springframework.beans.BeansException 如果获取失败
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return context.getBean(clazz);
    }
}
