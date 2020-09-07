package com.yves.spring.spring.context;


import com.yves.spring.spring.beans.BeanDefinitionRegistry;

import java.io.IOException;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-11:52
 */
public class AnnotationApplicationContext extends AbstractApplicationContext {
    private ClassPathBeanDefinitionScanner scanner;

    public AnnotationApplicationContext(String... basePackages) throws Throwable {
        //所有的BeanDefinitionRegistry类都是beanFactory的子类
        scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) this.beanFactory);
        //扫描包
        scanner.scan(basePackages);
    }


    @Override
    public Resource getResource(String location) throws IOException {
        return null;
    }
}
