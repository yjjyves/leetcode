package com.yves.spring.spring.context.annotation;


import com.yves.spring.spring.beans.BeanDefinition;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)//标记的注释由JVM保留，因此运行时环境可以使用它
@Target(ElementType.TYPE)//标记这个注解应该是哪种 Java 成员,可以应用于类的任何元素
@Documented//标记这些注解是否包含在用户文档中
public @interface Component {

    String value() default "";

    String name() default "";

    String scope() default BeanDefinition.SCOPE_SINGLETION;

    String factoryMethodName() default "";

    String factoryBeanName() default "";

    String initMethodName() default "";

    String destroyMethodName() default "";
}
