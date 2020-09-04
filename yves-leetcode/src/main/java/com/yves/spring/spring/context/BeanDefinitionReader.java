package com.yves.spring.spring.context;

public interface BeanDefinitionReader {

	void loadBeanDefintions(Resource resource) throws Throwable;

	void loadBeanDefintions(Resource... resource) throws Throwable;
}
