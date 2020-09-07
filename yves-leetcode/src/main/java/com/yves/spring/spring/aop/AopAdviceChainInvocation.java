package com.yves.spring.spring.aop;


import com.yves.spring.spring.aop.advice.AfterReturningAdvice;
import com.yves.spring.spring.aop.advice.MethodBeforeAdvice;
import com.yves.spring.spring.aop.advice.MethodInterceptor;

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
                //前置通知 只做前置处理,处理完成之后再处理下一个通知所以调用this.invoke,继续往下走走责任链
                ((MethodBeforeAdvice) advice).before(method, args, target);
            } else if (advice instanceof AfterReturningAdvice) {
                // 当是后置增强时，先得得到结果，再执行后置增强逻辑
                //此处相当于递归调用,层层嵌套,拿到最后一个后置通知的处理结果再处理前一个,最后一个执行return method.invoke(target, args)得到结果
                Object returnValue = this.invoke();
                ((AfterReturningAdvice) advice).afterReturning(returnValue, method, args, target);
                return returnValue;
            } else if (advice instanceof MethodInterceptor) {
                // 执行环绕增强和异常处理增强。注意这里给入的method 和 对象 是invoke方法和链对象

                /**
                 * 环绕通知 1 执行环绕通知的invoke方法，2 由于传入的是这个责任链本身所以在MethodInterceptor中invoke再调用又回到责任链中来,处理下一个通知,
                 * 但是此环绕通知此时并未处理完成,因为还需要处理责任链后面中的其他通知,最后在执行method.invoke(target, args);又回到MethodInterceptor中去，
                 * 等到所有其他通知都处理完,再执行环绕通知中的剩余逻辑
                 * @return
                 * @author yijinjin
                 * @date 2020/9/7 -15:05
                 */
                return ((MethodInterceptor) advice).invoke(invokeMethod, null, this);
            }
            return this.invoke();
        } else {
            return method.invoke(target, args);
        }
    }


}
