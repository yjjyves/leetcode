package com.yves.spring.spring.aop.advisor;

import com.yves.spring.aop.pointcut.Pointcut;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/26-18:39
 */
public interface PointcutAdvisor extends Advisor {
    Pointcut getPointcut();
}
