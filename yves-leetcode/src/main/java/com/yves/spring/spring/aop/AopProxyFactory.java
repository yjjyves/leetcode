package com.yves.spring.spring.aop;

import com.yves.spring.spring.aop.advisor.Advisor;
import com.yves.spring.spring.beans.BeanFactory;

import java.util.List;

public interface AopProxyFactory {
    //创建代理对象
    AopProxy createAopProxy(Object bean, String beanName, List<Advisor> advisors, BeanFactory beanFactory);

    static AopProxyFactory getDefaultAopProxyFactory() {
        return new DefaultAopProxyFactory();
    }
}
