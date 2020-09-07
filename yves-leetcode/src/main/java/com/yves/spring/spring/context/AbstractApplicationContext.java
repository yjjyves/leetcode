package com.yves.spring.spring.context;

import com.yves.spring.spring.beans.BeanFactory;
import com.yves.spring.spring.beans.BeanPostProcessor;
import com.yves.spring.spring.beans.impl.PreBuildBeanFactory;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-11:49
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    protected BeanFactory beanFactory;

    public AbstractApplicationContext() {
        super();
        this.beanFactory = new PreBuildBeanFactory();
    }

    @Override
    public Object getBean(String name) throws Throwable {
        return beanFactory.getBean(name);
    }

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor bpp) {
        beanFactory.registerBeanPostProcessor(bpp);
    }
}
