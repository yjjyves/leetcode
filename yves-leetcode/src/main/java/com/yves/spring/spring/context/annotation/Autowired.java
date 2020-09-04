package com.yves.spring.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-14:40
 */
//可以应用于字段,方法,构造方法,参数,注释类型
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
    boolean required() default true;
}
