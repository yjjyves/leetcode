package com.yves.spring.spring.beans;

/**
 * Bean 工厂要创建 bean，就得能获得 bean 定义信息。Bean 定义注册就是来完成向 bean 工厂 注册 bean 定义信息的。
 *
 * @author yijinjin
 * @date 2020/5/26-10:46
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception;

    BeanDefinition getBeanDefinition(String beanName);

    boolean containsBeanDefinition(String beanName);
}
