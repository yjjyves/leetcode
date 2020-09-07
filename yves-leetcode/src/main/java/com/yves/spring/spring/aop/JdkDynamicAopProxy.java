package com.yves.spring.spring.aop;

import com.yves.spring.spring.aop.advisor.Advisor;
import com.yves.spring.spring.beans.BeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-8:53
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    private static final Log logger = LogFactory.getLog(JdkDynamicAopProxy.class);

    private String beanName;
    private Object target;
    private List<Advisor> matchAdvisors;

    private BeanFactory beanFactory;

    public JdkDynamicAopProxy(String beanName, Object target, List<Advisor> matchAdvisors, BeanFactory beanFactory) {
        this.beanName = beanName;
        this.target = target;
        this.matchAdvisors = matchAdvisors;
        this.beanFactory = beanFactory;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        if (logger.isDebugEnabled()) {
            logger.debug("为" + target + "创建代理。");
        }
        return Proxy.newProxyInstance(classLoader, target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return AopProxyUtils.applyAdvices(target, method, args, matchAdvisors, proxy, beanFactory);
    }
}
