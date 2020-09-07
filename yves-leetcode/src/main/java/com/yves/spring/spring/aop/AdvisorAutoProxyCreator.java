package com.yves.spring.spring.aop;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.yves.spring.spring.aop.advisor.PointcutAdvisor;
import com.yves.spring.spring.aop.pointcut.Pointcut;
import com.yves.spring.spring.beans.BeanFactory;
import com.yves.spring.spring.beans.BeanFactoryAware;
import com.yves.spring.spring.beans.BeanPostProcessor;
import com.yves.spring.spring.aop.advisor.Advisor;
import com.yves.spring.spring.aop.advisor.AdvisorRegistry;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/26-19:21
 */
public class AdvisorAutoProxyCreator implements AdvisorRegistry, BeanPostProcessor, BeanFactoryAware {
    private List<Advisor> advisors;

    private BeanFactory beanFactory;

    public AdvisorAutoProxyCreator() {
        this.advisors = new ArrayList<>();
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void registAdvisor(Advisor ad) {
        advisors.add(ad);
    }

    @Override
    public List<Advisor> getAdvisors() {
        return advisors;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Throwable {
        // 在此判断bean是否需要进行切面增强
        List<Advisor> matchAdvisors = getMatchedAdvisors(bean, beanName);
        // 如需要就进行增强,再返回增强的对象。
        if (CollectionUtils.isNotEmpty(matchAdvisors)) {
            bean = this.createProxy(bean, beanName, matchAdvisors);
        }
        return bean;
    }

    private List<Advisor> getMatchedAdvisors(Object bean, String beanName) {
        if (CollectionUtils.isEmpty(advisors)) {
            return null;
        }

        // 得到类、所有的方法
        Class<?> beanClass = bean.getClass();
        List<Method> allMethods = this.getAllMethodForClass(beanClass);

        // 存放匹配的Advisor的list
        List<Advisor> matchAdvisors = new ArrayList<>();
        // 遍历Advisor来找匹配的
        for (Advisor ad : this.advisors) {
            if (ad instanceof PointcutAdvisor) {
                if (isPointcutMatchBean((PointcutAdvisor) ad, beanClass, allMethods)) {
                    matchAdvisors.add(ad);
                }
            }
        }

        return matchAdvisors;
    }

    private List<Method> getAllMethodForClass(Class<?> beanClass) {
        List<Method> allMethods = new LinkedList<>();
        //首先获取对应类的所有接口并连同类本身一起遍历
        Set<Class<?>> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesForClassAsSet(beanClass));
        classes.add(beanClass);
        for (Class<?> clazz : classes) {
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
            for (Method m : methods) {
                allMethods.add(m);
            }
        }

        return allMethods;
    }

    private boolean isPointcutMatchBean(PointcutAdvisor pa, Class<?> beanClass, List<Method> methods) {
        Pointcut p = pa.getPointcut();

        // 首先判断类是否匹配
        if (!p.matchsClass(beanClass)) {
            return false;
        }

        // 再判断是否有方法匹配
        for (Method method : methods) {
            if (p.matchsMethod(method, beanClass)) {
                return true;
            }
        }
        return false;
    }

    private Object createProxy(Object bean, String beanName, List<Advisor> matchAdvisors) throws Throwable {
        // 通过AopProxyFactory工厂去完成选择、和创建代理对象的工作。
        return AopProxyFactory.getDefaultAopProxyFactory().createAopProxy(bean, beanName, matchAdvisors, beanFactory)
                .getProxy();
    }
}

