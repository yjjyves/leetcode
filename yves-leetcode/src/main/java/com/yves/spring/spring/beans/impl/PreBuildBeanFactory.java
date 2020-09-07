package com.yves.spring.spring.beans.impl;

import com.yves.spring.spring.beans.BeanDefinition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/26-11:29
 */
public class PreBuildBeanFactory extends DefaultBeanFactory {
    private final Log logger = LogFactory.getLog(getClass());

    //所有bean名称
    private List<String> beanNames = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
            throws Exception {
        super.registerBeanDefinition(beanName, beanDefinition);
        synchronized (beanNames) {
            beanNames.add(beanName);
        }
    }

    //提前初始化单例bean
    public void preInstantiateSingletons() throws Throwable  {
        synchronized (beanNames) {
            for (String name : beanNames) {
                BeanDefinition bd = this.getBeanDefinition(name);
                if (bd.isSingleton()) {
                    this.doGetBean(name);
                    if (logger.isDebugEnabled()) {
                        logger.debug("preInstantiate: name=" + name + " " + bd);
                    }
                }
            }
        }
    }
}
