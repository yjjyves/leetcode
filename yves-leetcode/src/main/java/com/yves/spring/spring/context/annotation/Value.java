package com.yves.spring.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-14:50
 */

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
    String value();
}
