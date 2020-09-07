package com.yves.spring.spring.context;

import com.yves.spring.spring.beans.BeanDefinition;
import com.yves.spring.spring.beans.BeanDefinitionRegistry;
import com.yves.spring.spring.beans.BeanReference;
import com.yves.spring.spring.beans.impl.GenericBeanDefinition;
import com.yves.spring.spring.context.annotation.Autowired;
import com.yves.spring.spring.context.annotation.Component;
import com.yves.spring.spring.context.annotation.Qualifier;
import com.yves.spring.spring.context.annotation.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-11:57
 */
public class AnnotationBeanDefintionReader extends AbstractBeanDefinitionReader {
    public AnnotationBeanDefintionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    @Override
    public void loadBeanDefintions(Resource resource) throws Throwable {
        this.loadBeanDefintions(new Resource[]{resource});
    }

    @Override
    public void loadBeanDefintions(Resource... resource) throws Throwable {
        if (resource != null && resource.length > 0) {
            for (Resource r : resource) {
                retriveAndRegistBeanDefinition(r);
            }
        }
    }

    private void retriveAndRegistBeanDefinition(Resource resource) throws Throwable {
        if (resource != null && resource.getFile() != null) {
            String className = getClassNameFromFile(resource.getFile());
            try {
                Class<?> clazz = Class.forName(className);
                Component component = clazz.getAnnotation(Component.class);
                if (component != null) {// 标注了@Component注解
                    GenericBeanDefinition bd = new GenericBeanDefinition();
                    bd.setBeanClass(clazz);
                    bd.setScope(component.scope());
                    bd.setFactoryMethodName(component.factoryMethodName());
                    bd.setFactoryBeanName(component.factoryBeanName());
                    bd.setInitMethodName(component.initMethodName());
                    bd.setDestroyMethodName(component.destroyMethodName());

                    // 获得所有构造方法，在构造方法上找@Autowired注解，如有，将这个构造方法set到bd;
                    this.handleConstructor(clazz, bd);

                    // 处理工厂方法参数依赖
                    if (StringUtils.isNotBlank(bd.getFactoryMethodName())) {
                        this.handleFactoryMethodArgs(clazz, bd);
                    }
                    // 处理属性依赖
                    this.handlePropertyDi(clazz, bd);

                    String beanName = "".equals(component.value()) ? component.name() : null;
                    if (StringUtils.isBlank(beanName)) {
                        // TODO 应用名称生成规则生成beanName;
                        beanName = buildDefaultBeanName(className);
                    }
                    // 注册bean定义
                    this.registry.registerBeanDefinition(beanName, bd);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void handlePropertyDi(Class<?> clazz, GenericBeanDefinition bd) {
        // TODO Auto-generated method stub

    }

    private void handleFactoryMethodArgs(Class<?> clazz, GenericBeanDefinition bd) {
        // TODO Auto-generated method stub

    }

    private void handleConstructor(Class<?> clazz, GenericBeanDefinition bd) {
        // 获得所有构造方法，在构造方法上找@Autowired注解，如有，将这个构造方法set到bd;
        Constructor<?>[] cs = clazz.getConstructors();
        if (cs != null && cs.length > 0) {
            for (Constructor<?> c : cs) {
                if (c.getAnnotation(Autowired.class) != null) {
                    bd.setConstructor(c);
                    Parameter[] ps = c.getParameters();
                    // TDDO 遍历获取参数上的注解，及创建参数依赖
                    List<Object> constructorArgumentValues = getConstructorArgumentValues(ps);
                    bd.setConstructorArgumentValues(constructorArgumentValues);
                    break;
                }
            }
        }
    }

    private List<Object> getConstructorArgumentValues(Parameter[] ps) {
        if (ps == null || ps.length <= 0) {
            return null;
        }
        List<Object> args = new ArrayList<>();
        for (Parameter par : ps) {
            if (par.getAnnotation(Value.class) != null) {
                String value = par.getAnnotation(Value.class).value();
                args.add(value);
            }
            if (par.getAnnotation(Qualifier.class) != null) {
                String value = par.getAnnotation(Qualifier.class).value();
                args.add(new BeanReference(value));
            }
        }
        return args;
    }

    private int classPathAbsLength = AnnotationBeanDefintionReader.class.getResource("/").getPath().length();

    private String getClassNameFromFile(File file) {
        String absPath = file.getAbsolutePath();
        String name = absPath.substring(classPathAbsLength - 1, absPath.indexOf('.'));
        return StringUtils.replace(name, File.separator, ".");
    }

    protected String buildDefaultBeanName(String className) {
        String shortClassName = ClassUtils.getShortName(className);
        return Introspector.decapitalize(shortClassName);
    }

}
