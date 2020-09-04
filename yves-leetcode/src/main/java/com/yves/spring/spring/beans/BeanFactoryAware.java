package com.yves.spring.spring.beans;

public interface BeanFactoryAware extends Aware {

	void setBeanFactory(BeanFactory bf);
}
