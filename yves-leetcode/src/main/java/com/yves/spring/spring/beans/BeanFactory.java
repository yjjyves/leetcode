package com.yves.spring.spring.beans;


/**
 * 创建、管理 bean。它是一个工厂，负责对外提供 bean
 *
 * @author yijinjin
 * @date 2020/5/26 -10:47
 */
public interface BeanFactory {
    Object getBean(String name) throws Throwable;

    void registerBeanPostProcessor(BeanPostProcessor bpp);
}
