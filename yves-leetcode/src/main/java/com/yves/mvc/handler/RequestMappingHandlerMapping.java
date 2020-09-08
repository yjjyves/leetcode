package com.yves.mvc.handler;

import com.yves.mvc.annotation.RequestMappingInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/15-15:08
 */
public class RequestMappingHandlerMapping implements HandlerMapping, InitializingBean, ApplicationContextAware {
    private ApplicationContext applicationContext;

    private Set<RequestMappingInfo> requestMappingInfos = new HashSet<>();

    private Map<String, List<RequestMappingInfo>> urlMaps = new HashMap<>();

    public ApplicationContext obtainApplicationContext() {
        return this.applicationContext;
    }


    @Override
    public Object getHandler(HttpServletRequest request) throws Exception {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 检测@Controller Bean
        for (String beanName : this.applicationContext.getBeanNamesForType(Object.class)) {
            Class<?> beanType = this.applicationContext.getType(beanName);
            if (isHandlerBean(beanType)) {
                detectHandlerMethod(beanType);
            }
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    private boolean isHandlerBean(Class<?> beanType) {
        // TODO Auto-generated method stub
        return false;
    }

    private void detectHandlerMethod(Class<?> beanType) {
        // TODO Auto-generated method stub

    }

}
