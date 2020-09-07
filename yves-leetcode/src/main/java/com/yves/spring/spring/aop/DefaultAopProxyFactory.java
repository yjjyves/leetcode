package com.yves.spring.spring.aop;

import com.yves.spring.spring.aop.advisor.Advisor;
import com.yves.spring.spring.beans.BeanFactory;

import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-8:49
 */
public class DefaultAopProxyFactory implements AopProxyFactory {
    @Override
    public AopProxy createAopProxy(Object bean, String beanName, List<Advisor> advisors, BeanFactory beanFactory) {
        // 是该用jdk动态代理还是cglib？
        if (shouldUseJDKDynamicProxy(bean, beanName)) {
            return new JdkDynamicAopProxy(beanName, bean, advisors, beanFactory);
        }else {
            return new CglibDynamicAopProxy(beanName, bean, advisors, beanFactory);
        }
    }

    private boolean shouldUseJDKDynamicProxy(Object bean, String beanName) {
        // 如何判断？
        // 这样可以吗：有实现接口就用JDK,没有就用cglib？
        // 请同学们在读spring的源码时看spring中如何来判断的
        return false;
    }
}
