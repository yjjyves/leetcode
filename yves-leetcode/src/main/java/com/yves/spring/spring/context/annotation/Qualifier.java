package com.yves.spring.spring.context.annotation;


import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Documented
@Inherited//表明注释类型可以从超类继承
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {

    String value() default "";
}
