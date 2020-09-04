package com.yves.spring.spring.context;

import com.yves.spring.beans.BeanDefinitionRegistry;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-16:11
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }


    @Override
    public void loadBeanDefintions(Resource resource) throws Throwable {

    }

    @Override
    public void loadBeanDefintions(Resource... resource) throws Throwable {
        if (resource == null || resource.length <= 0) {
            return;
        }
        for (Resource res : resource) {
            this.parseXml(res);
        }
    }

    private void parseXml(Resource r) {
        // TODO 解析xml文档，获取bean定义 ，创建bean定义对象，注册到BeanDefinitionRegistry中。
    }
}
