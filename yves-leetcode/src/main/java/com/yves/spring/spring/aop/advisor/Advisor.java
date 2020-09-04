package com.yves.spring.spring.aop.advisor;

/**
 * 通知者 切面
 */
public interface Advisor {

    String getAdviceBeanName();

    String getExpression();

}
