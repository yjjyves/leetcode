package com.yves.spring.spring.aop.advice;

import java.lang.reflect.Method;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/26-16:53
 */
public interface MethodBeforeAdvice extends Advice {
    /**
     * 实现该方法进行前置增强
     *
     * @param method
     *            被增强的方法
     * @param args
     *            方法的参数
     * @param target
     *            被增强的目标对象
     * @throws Throwable
     */
    void before(Method method, Object[] args, Object target) throws Throwable;
}
