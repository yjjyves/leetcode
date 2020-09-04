package com.yves.spring.spring.beans;


import lombok.Data;

/**
 * 用于依赖注入中描述bean依赖
 *
 * @author yijinjin
 * @date 2020/5/26 -11:30
 */
@Data
public class BeanReference {
    // 引用的beanName
    private String beanName;

    public BeanReference(String beanName) {
        super();
        this.beanName = beanName;
    }
}
