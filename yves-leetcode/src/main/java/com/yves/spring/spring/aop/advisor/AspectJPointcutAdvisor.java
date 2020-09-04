package com.yves.spring.spring.aop.advisor;

import com.yves.spring.aop.pointcut.AspectJExpressionPointcut;
import com.yves.spring.aop.pointcut.Pointcut;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/26-18:44
 */
public class AspectJPointcutAdvisor implements PointcutAdvisor {
    // 切面类
    private String adviceBeanName;

    //切入点表达式
    private String expression;

    private AspectJExpressionPointcut pointcut;

    public AspectJPointcutAdvisor(String adviceBeanName, String expression) {
        super();
        this.adviceBeanName = adviceBeanName;
        this.expression = expression;
        this.pointcut = new AspectJExpressionPointcut(this.expression);
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public String getAdviceBeanName() {
        return adviceBeanName;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}
