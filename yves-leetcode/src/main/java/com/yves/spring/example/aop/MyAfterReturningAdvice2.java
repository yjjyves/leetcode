package com.yves.spring.example.aop;


import com.yves.spring.aop.advice.AfterReturningAdvice;

import java.lang.reflect.Method;

public class MyAfterReturningAdvice2 implements AfterReturningAdvice {

	@Override
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		System.out.println(this + " 对 " + target + " 做了后置增强2，得到的返回值=" + returnValue);
	}

}
