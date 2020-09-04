package com.yves.spring.spring.aop;

import com.yves.spring.aop.advice.AfterReturningAdvice;
import com.yves.spring.aop.advice.MethodBeforeAdvice;
import com.yves.spring.aop.advice.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-9:35
 */
public class AopAdviceChainInvocation {
    //需要增强的方法
    private static Method invokeMethod;

    //???????
    static {
        try {
            invokeMethod = AopAdviceChainInvocation.class.getMethod("invoke", null);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private Object proxy;
    private Object target;
    private Method method;
    private Object[] args;
    private List<Object> advices;

    public AopAdviceChainInvocation(Object proxy, Object target, Method method, Object[] args, List<Object> advices) {
        super();
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.args = args;
        this.advices = advices;
    }

    // 责任链执行记录索引号
    private int i = 0;

    public Object invoke() throws Throwable {
        if (i < advices.size()) {
            Object advice = advices.get(i++);
            if (advice instanceof MethodBeforeAdvice) {
                ((MethodBeforeAdvice) advice).before(method, args, target);
            } else if (advice instanceof AfterReturningAdvice) {
                // 当是后置增强时，先得得到结果，再执行后置增强逻辑
                Object returnValue = this.invoke();
                ((AfterReturningAdvice) advice).afterReturning(returnValue, method, args, target);
                return returnValue;
            } else if (advice instanceof MethodInterceptor) {
                // 执行环绕增强和异常处理增强。注意这里给入的method 和 对象 是invoke方法和链对象
                return ((MethodInterceptor) advice).invoke(invokeMethod, null, this);
            }
            return this.invoke();
        } else {
            return method.invoke(target, args);
        }
    }


}
