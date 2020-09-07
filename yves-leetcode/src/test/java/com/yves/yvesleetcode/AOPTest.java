package com.yves.yvesleetcode;

import java.util.ArrayList;
import java.util.List;

import com.yves.spring.example.ABean;
import com.yves.spring.example.CBean;
import com.yves.spring.example.aop.MyAfterReturningAdvice;
import com.yves.spring.example.aop.MyBeforeAdvice;
import com.yves.spring.example.aop.MyMethodInterceptor;
import com.yves.spring.spring.aop.AdvisorAutoProxyCreator;
import com.yves.spring.spring.aop.advisor.AspectJPointcutAdvisor;
import com.yves.spring.spring.beans.BeanReference;
import com.yves.spring.spring.beans.impl.GenericBeanDefinition;
import com.yves.spring.spring.beans.impl.PreBuildBeanFactory;
import org.junit.Test;


public class AOPTest {

	static PreBuildBeanFactory bf = new PreBuildBeanFactory();

	@Test
	public void testCirculationDI() throws Throwable {

		GenericBeanDefinition bd = new GenericBeanDefinition();
		bd.setBeanClass(ABean.class);
		List<Object> args = new ArrayList<>();
		args.add("abean01");
		args.add(new BeanReference("cbean"));
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("abean", bd);

		bd = new GenericBeanDefinition();
		bd.setBeanClass(CBean.class);
		args = new ArrayList<>();
		args.add("cbean01");
		bd.setConstructorArgumentValues(args);
		bf.registerBeanDefinition("cbean", bd);

		// 前置增强advice bean注册
		bd = new GenericBeanDefinition();
		bd.setBeanClass(MyBeforeAdvice.class);
		bf.registerBeanDefinition("myBeforeAdvice", bd);

		// 环绕增强advice bean注册
		bd = new GenericBeanDefinition();
		bd.setBeanClass(MyMethodInterceptor.class);
		bf.registerBeanDefinition("myMethodInterceptor", bd);

		// 后置增强advice bean注册
		bd = new GenericBeanDefinition();
		bd.setBeanClass(MyAfterReturningAdvice.class);
		bf.registerBeanDefinition("myAfterReturningAdvice", bd);

		// 往BeanFactory中注册AOP的BeanPostProcessor
		AdvisorAutoProxyCreator aapc = new AdvisorAutoProxyCreator();
		bf.registerBeanPostProcessor(aapc);
		// 向AdvisorAutoProxyCreator注册Advisor
		aapc.registAdvisor(
				new AspectJPointcutAdvisor("myBeforeAdvice", "execution(* com.yves.spring.example.ABean.*(..))"));
		// 向AdvisorAutoProxyCreator注册Advisor
		aapc.registAdvisor(
				new AspectJPointcutAdvisor("myMethodInterceptor", "execution(* com.yves.spring.example.ABean.do*(..))"));
		// 向AdvisorAutoProxyCreator注册Advisor
		aapc.registAdvisor(new AspectJPointcutAdvisor("myAfterReturningAdvice",
				"execution(* com.yves.spring.example.ABean.do*(..))"));

		bf.preInstantiateSingletons();

		ABean abean = (ABean) bf.getBean("abean");

		abean.doSomthing();
		System.out.println("--------------------------------");
		abean.sayHello();
	}
}
