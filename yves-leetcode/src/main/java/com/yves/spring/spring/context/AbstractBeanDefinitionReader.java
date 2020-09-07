package com.yves.spring.spring.context;

import com.yves.spring.spring.beans.BeanDefinitionRegistry;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-11:57
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {
    protected BeanDefinitionRegistry registry;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super();
        this.registry = registry;
    }
}
