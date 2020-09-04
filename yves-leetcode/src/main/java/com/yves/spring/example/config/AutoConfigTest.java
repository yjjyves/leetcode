package com.yves.spring.example.config;

import com.yves.example.ABean;
import com.yves.spring.context.AnnotationApplicationContext;
import org.junit.Test;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-16:57
 */
public class AutoConfigTest {

    public static void main(String[] args) throws Throwable {

        AnnotationApplicationContext applicationContext = new AnnotationApplicationContext("com.yves.example");

        ABean aBean = (ABean) applicationContext.getBean("ABean");

        aBean.doSomthing();

    }
}
