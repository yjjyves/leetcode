package com.yves.spring.spring.aop;

import com.yves.spring.spring.aop.advisor.Advisor;
import com.yves.spring.spring.beans.BeanDefinition;
import com.yves.spring.spring.beans.BeanFactory;
import com.yves.spring.spring.beans.impl.DefaultBeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-8:59
 */
public class CglibDynamicAopProxy implements AopProxy, MethodInterceptor {

    private static final Log logger = LogFactory.getLog(CglibDynamicAopProxy.class);
    private static Enhancer enhancer = new Enhancer();

    private String beanName;
    private Object target;

    private List<Advisor> matchAdvisors;

    private BeanFactory beanFactory;

    public CglibDynamicAopProxy(String beanName, Object target, List<Advisor> matchAdvisors, BeanFactory beanFactory) {
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
            logger.debug("为" + target + "创建cglib代理");
        }
        Class<?> superClass = this.target.getClass();
        enhancer.setSuperclass(superClass);
        enhancer.setInterfaces(this.getClass().getInterfaces());
        enhancer.setCallback(this);

        Constructor<?> constructor = null;
        try {
            constructor = superClass.getConstructor(new Class<?>[]{});
        } catch (NoSuchMethodException | SecurityException e) {
        }
        if (constructor != null) {
            return enhancer.create();
        } else {
            BeanDefinition beanDefinition = ((DefaultBeanFactory) beanFactory).getBeanDefinition(beanName);
            return enhancer.create(beanDefinition.getConstructor().getParameterTypes(), beanDefinition.getConstructorArgumentRealValues());
        }
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return AopProxyUtils.applyAdvices(target, method, objects, matchAdvisors, methodProxy, beanFactory);
    }
}
