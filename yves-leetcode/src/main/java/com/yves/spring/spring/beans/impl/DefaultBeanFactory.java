package com.yves.spring.spring.beans.impl;

import com.yves.spring.spring.beans.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/26-10:51
 */
public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry, Closeable {
    private final Log logger = LogFactory.getLog(getClass());

    // 用来存放bean
    private ConcurrentHashMap<String, Object> beanMap = new ConcurrentHashMap<>();

    // 用来存放beanDefinition
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    //记录正在创建的bean
    private ThreadLocal<Set<String>> buildingBeans = new ThreadLocal<>();

    private List<BeanPostProcessor> beanPostProcessors = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void registerBeanPostProcessor(BeanPostProcessor bpp) {
        this.beanPostProcessors.add(bpp);
        if (bpp instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bpp).setBeanFactory(this);
        }
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {

        Objects.requireNonNull(beanName, "注册bean需要给入beanName");
        Objects.requireNonNull(beanDefinition, "注册bean需要给入beanDefinition");

        // 校验给入的bean是否合法
        if (!beanDefinition.validate()) {
            throw new BeanDefinitionRegistException("名字为[" + beanName + "] 的bean定义不合法：" + beanDefinition);
        }

        if (this.containsBeanDefinition(beanName)) {
            throw new BeanDefinitionRegistException(
                    "名字为[" + beanName + "] 的bean定义已存在:" + this.getBeanDefinition(beanName));
        }
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }


    @Override
    public Object getBean(String name) throws Throwable {
        return doGetBean(name);
    }


    protected Object doGetBean(String beanName) throws Throwable {
        Assert.notNull(beanName, "beanName must not be null");
        Object bean = beanMap.get(beanName);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        Objects.requireNonNull(beanDefinition, "不存在name为：" + beanName + " 的bean定义！");

        // 检测循环依赖
        Set<String> ingBeans = buildingBeans.get();

        if (ingBeans == null) {
            ingBeans = new HashSet<>();
            this.buildingBeans.set(ingBeans);
        }

        if (ingBeans.contains(beanName)) {
            throw new Exception(beanName + " 循环依赖！" + ingBeans);
        }

        // 记录正在创建的Bean
        ingBeans.add(beanName);

        Class<?> type = beanDefinition.getBeanClass();
        if (type != null) {
            //是否指定了工厂方法,没有指定则用构造方法创建
            if (StringUtils.isBlank(beanDefinition.getFactoryMethodName())) {
                // 构造方法来构造对象
                bean = this.createInstanceByConstructor(beanDefinition);
            } else {
                // 静态工厂方法
                bean = this.createInstanceByStaticFactoryMethod(beanDefinition);
            }
        } else {
            // 工厂bean方式来构造对象
            bean = this.createInstanceByFactoryBean(beanDefinition);
        }

        // 创建好实例后，移除创建中记录
        ingBeans.remove(beanName);

        // 给入属性依赖
        this.setPropertyDIValues(beanDefinition, bean);

        // 应用bean初始化前的处理
        bean = this.applyPostProcessBeforeInitialization(bean, beanName);

        // 执行初始化方法
        this.doInit(beanDefinition, bean);

        // 应用bean初始化后的处理
        bean = this.applyPostProcessAfterInitialization(bean, beanName);

        //如果是单例 存到map中
        if (beanDefinition.isSingleton()) {
            beanMap.put(beanName, bean);
        }

        return bean;
    }

    // 应用bean初始化前的处理
    private Object applyPostProcessBeforeInitialization(Object bean, String beanName) throws Throwable {
        for (BeanPostProcessor bpp : this.beanPostProcessors) {
            bean = bpp.postProcessBeforeInitialization(bean, beanName);
        }
        return bean;
    }

    // 应用bean初始化后的处理
    private Object applyPostProcessAfterInitialization(Object bean, String beanName) throws Throwable {
        for (BeanPostProcessor bpp : this.beanPostProcessors) {
            bean = bpp.postProcessAfterInitialization(bean, beanName);
        }
        return bean;
    }

    private void setPropertyDIValues(BeanDefinition beanDefinition, Object bean) throws Throwable {
        if (CollectionUtils.isEmpty(beanDefinition.getPropertyValues())) {
            return;
        }
        for (PropertyValue property : beanDefinition.getPropertyValues()) {
            if (StringUtils.isEmpty(property.getName())) {
                continue;
            }
            Class<?> clazz = bean.getClass();
            Field field = clazz.getDeclaredField(property.getName());
            field.setAccessible(true);

            //根据value设置值
            Object rv = property.getValue();
            Object v = null;
            if (rv == null) {
                v = null;
            } else if (rv instanceof BeanReference) {
                v = this.doGetBean(((BeanReference) rv).getBeanName());
            } else if (rv instanceof Object[]) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Collection) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Properties) {
                // TODO 处理properties中的bean引用
            } else if (rv instanceof Map) {
                // TODO 处理Map中的bean引用
            } else {
                v = rv;
            }

            field.set(bean, v);
        }
    }

    @Override
    public void close() throws IOException {
        // 执行单例实例的销毁方法
        for (Map.Entry<String, BeanDefinition> e : this.beanDefinitionMap.entrySet()) {
            String beanName = e.getKey();
            BeanDefinition bd = e.getValue();

            if (bd.isSingleton() && StringUtils.isNotBlank(bd.getDestroyMethodName())) {
                Object instance = this.beanMap.get(beanName);
                try {
                    Method m = instance.getClass().getMethod(bd.getDestroyMethodName(), null);
                    m.invoke(instance, null);
                } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e1) {
                    logger.error("执行bean[" + beanName + "] " + bd + " 的 销毁方法异常！", e1);
                }
            }
        }
    }

    // 构造方法来构造对象
    private Object createInstanceByConstructor(BeanDefinition bd)
            throws Throwable {
        try {
            Object[] args = this.getConstructorArgumentValues(bd);
            if (args == null) {
                return bd.getBeanClass().newInstance();
            } else {
                bd.setConstructorArgumentRealValues(args);
                // 决定构造方法
                Constructor<?> constructor = this.determineConstructor(bd, args);
                // 缓存构造函数由determineConstructor 中移到了这里，无论原型否都缓存，因为后面AOP需要用
                bd.setConstructor(constructor);
                // 决定构造方法
                return constructor.newInstance(args);
            }
        } catch (SecurityException e1) {
            logger.error("创建bean的实例异常,beanDefinition：" + bd, e1);
            throw e1;
        }
    }

    // 静态工厂方法
    private Object createInstanceByStaticFactoryMethod(BeanDefinition bd) throws Throwable {

        Class<?> type = bd.getBeanClass();
        Object[] realArgs = this.getRealValues(bd.getConstructorArgumentValues());
        Method m = this.determineFactoryMethod(bd, realArgs, null);
        return m.invoke(type, realArgs);
    }

    // 工厂bean方式来构造对象
    private Object createInstanceByFactoryBean(BeanDefinition bd) throws Throwable {

        Object factoryBean = this.doGetBean(bd.getFactoryBeanName());
        Object[] realArgs = this.getRealValues(bd.getConstructorArgumentValues());
        Method m = this.determineFactoryMethod(bd, realArgs, factoryBean.getClass());

        return m.invoke(factoryBean, realArgs);
    }

    /**
     * 执行初始化方法
     *
     * @param bd
     * @param instance
     * @throws Exception
     */
    private void doInit(BeanDefinition bd, Object instance) throws Throwable {
        // 执行初始化方法
        if (StringUtils.isNotBlank(bd.getInitMethodName())) {
            Method m = instance.getClass().getMethod(bd.getInitMethodName(), null);
            m.invoke(instance, null);
        }
    }

    private Object[] getConstructorArgumentValues(BeanDefinition bd) throws Throwable {

        return this.getRealValues(bd.getConstructorArgumentValues());
    }

    private Object[] getRealValues(List<?> defs) throws Throwable {
        if (CollectionUtils.isEmpty(defs)) {
            return null;
        }

        Object[] values = new Object[defs.size()];
        int i = 0;
        Object v = null;
        for (Object rv : defs) {
            if (rv == null) {
                v = null;
            } else if (rv instanceof BeanReference) {
                v = this.doGetBean(((BeanReference) rv).getBeanName());
            } else if (rv instanceof Object[]) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Collection) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Properties) {
                // TODO 处理properties中的bean引用
            } else if (rv instanceof Map) {
                // TODO 处理Map中的bean引用
            } else {
                v = rv;
            }

            values[i++] = v;
        }

        return values;
    }

    /**
     * 1、先根据参数的类型进行精确匹配查找，如未找到，则进行第 2 步查找；
     * 2、获得所有的构造方法，遍历，通过参数数量过滤，再比对形参类型与实参类型。
     *
     * @param bd
     * @param args
     * @return
     * @throws Exception
     */
    private Constructor<?> determineConstructor(BeanDefinition bd, Object[] args) throws Exception {

        Constructor<?> ct = null;

        if (args == null) {
            return bd.getBeanClass().getConstructor(null);
        }

        // 对于原型bean,从第二次开始获取bean实例时，可直接获得第一次缓存的构造方法。
        ct = bd.getConstructor();
        if (ct != null) {
            return ct;
        }

        // 根据参数类型获取精确匹配的构造方法
        Class<?>[] paramTypes = new Class[args.length];
        int j = 0;
        for (Object p : args) {
            paramTypes[j++] = p.getClass();
        }
        try {
            ct = bd.getBeanClass().getConstructor(paramTypes);
        } catch (Exception e) {
            // 这个异常不需要处理
        }

        if (ct == null) {

            // 没有精确参数类型匹配的，则遍历匹配所有的构造方法
            // 判断逻辑：先判断参数数量，再依次比对形参类型与实参类型
            outer:
            for (Constructor<?> ct0 : bd.getBeanClass().getConstructors()) {
                Class<?>[] paramterTypes = ct0.getParameterTypes();
                if (paramterTypes.length == args.length) {
                    for (int i = 0; i < paramterTypes.length; i++) {
                        if (!paramterTypes[i].isAssignableFrom(args[i].getClass())) {
                            continue outer;
                        }
                    }

                    ct = ct0;
                    break outer;
                }
            }
        }

        if (ct != null) {
            // 对于原型bean,可以缓存找到的构造方法，方便下次构造实例对象。在BeanDefinfition中获取设置所用构造方法的方法。
            // 同时在上面增加从beanDefinition中获取的逻辑。
            if (bd.isPrototype()) {
                bd.setConstructor(ct);
            }
            return ct;
        } else {
            throw new Exception("不存在对应的构造方法！" + bd);
        }
    }

    private Method determineFactoryMethod(BeanDefinition bd, Object[] args, Class<?> type) throws Exception {
        if (type == null) {
            type = bd.getBeanClass();
        }

        String methodName = bd.getFactoryMethodName();

        if (args == null) {
            return type.getMethod(methodName, null);
        }

        Method m = null;
        // 对于原型bean,从第二次开始获取bean实例时，可直接获得第一次缓存的构造方法。
        m = bd.getFactoryMethod();
        if (m != null) {
            return m;
        }

        // 根据参数类型获取精确匹配的方法
        Class[] paramTypes = new Class[args.length];
        int j = 0;
        for (Object p : args) {
            paramTypes[j++] = p.getClass();
        }
        try {
            m = type.getMethod(methodName, paramTypes);
        } catch (Exception e) {
            // 这个异常不需要处理
        }

        if (m == null) {

            // 没有精确参数类型匹配的，则遍历匹配所有的方法
            // 判断逻辑：先判断参数数量，再依次比对形参类型与实参类型
            outer:
            for (Method m0 : type.getMethods()) {
                if (!m0.getName().equals(methodName)) {
                    continue;
                }
                Class<?>[] paramterTypes = m.getParameterTypes();
                if (paramterTypes.length == args.length) {
                    for (int i = 0; i < paramterTypes.length; i++) {
                        if (!paramterTypes[i].isAssignableFrom(args[i].getClass())) {
                            continue outer;
                        }
                    }

                    m = m0;
                    break outer;
                }
            }
        }

        if (m != null) {
            // 对于原型bean,可以缓存找到的方法，方便下次构造实例对象。在BeanDefinfition中获取设置所用方法的方法。
            // 同时在上面增加从beanDefinition中获取的逻辑。
            if (bd.isPrototype()) {
                bd.setFactoryMethod(m);
            }
            return m;
        } else {
            throw new Exception("不存在对应的构造方法！" + bd);
        }
    }

}
