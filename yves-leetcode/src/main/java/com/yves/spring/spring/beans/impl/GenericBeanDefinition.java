package com.yves.spring.spring.beans.impl;

import com.yves.spring.beans.BeanDefinition;
import com.yves.spring.beans.PropertyValue;
import lombok.Data;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/26-10:50
 */
@Data
public class GenericBeanDefinition implements BeanDefinition {
    // bean类名称
    private volatile Class<?> beanClass;

    private volatile String scope = BeanDefinition.SCOPE_SINGLETION;

    private volatile String factoryBeanName;

    private volatile String factoryMethodName;

    private volatile String initMethodName;

    private volatile String destroyMethodName;

    private Constructor<?> constructor;

    private Method factoryMethod;

    private List<?> constructorArgumentValues;

    private List<PropertyValue> propertyValues;

    private ThreadLocal<Object[]> realConstructorArgumentValues = new ThreadLocal<>();

    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public String getScope() {
        return getScope();
    }

    @Override
    public boolean isSingleton() {
        return BeanDefinition.SCOPE_SINGLETION.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return BeanDefinition.SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    @Override
    public String getInitMethodName() {
        return initMethodName;
    }

    @Override
    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    @Override
    public List<?> getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    @Override
    public Object[] getConstructorArgumentRealValues() {
        return realConstructorArgumentValues.get();
    }

    @Override
    public void setConstructorArgumentRealValues(Object[] values) {
        realConstructorArgumentValues.set(values);
    }

    @Override
    public String toString() {
        return "GenericBeanDefinition [beanClass=" + beanClass + ", scope=" + scope + ", factoryBeanName="
                + factoryBeanName + ", factoryMethodName=" + factoryMethodName + ", initMethodName=" + initMethodName
                + ", destroyMethodName=" + destroyMethodName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((beanClass == null) ? 0 : beanClass.hashCode());
        result = prime * result + ((destroyMethodName == null) ? 0 : destroyMethodName.hashCode());
        result = prime * result + ((factoryBeanName == null) ? 0 : factoryBeanName.hashCode());
        result = prime * result + ((factoryMethodName == null) ? 0 : factoryMethodName.hashCode());
        result = prime * result + ((initMethodName == null) ? 0 : initMethodName.hashCode());
        result = prime * result + ((scope == null) ? 0 : scope.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenericBeanDefinition other = (GenericBeanDefinition) obj;
        if (beanClass == null) {
            if (other.beanClass != null)
                return false;
        } else if (!beanClass.equals(other.beanClass))
            return false;
        if (destroyMethodName == null) {
            if (other.destroyMethodName != null)
                return false;
        } else if (!destroyMethodName.equals(other.destroyMethodName))
            return false;
        if (factoryBeanName == null) {
            if (other.factoryBeanName != null)
                return false;
        } else if (!factoryBeanName.equals(other.factoryBeanName))
            return false;
        if (factoryMethodName == null) {
            if (other.factoryMethodName != null)
                return false;
        } else if (!factoryMethodName.equals(other.factoryMethodName))
            return false;
        if (initMethodName == null) {
            if (other.initMethodName != null)
                return false;
        } else if (!initMethodName.equals(other.initMethodName))
            return false;
        if (scope == null) {
            if (other.scope != null)
                return false;
        } else if (!scope.equals(other.scope))
            return false;
        return true;
    }
}


