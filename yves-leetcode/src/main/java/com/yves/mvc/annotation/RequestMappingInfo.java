package com.yves.mvc.annotation;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/15-9:34
 */
@Data
public class RequestMappingInfo {
    private String beanName;

    private Class<?> beanType;

    private Method method;

    private Object handler;

    private RequestMapping methodRequestMapping;

    private RequestMapping classRequestMapping;

    public RequestMappingInfo(RequestMapping classRequestMapping, String beanName, Class beanType,
                              RequestMapping methodRequestMapping, Method method) {
        super();
        this.classRequestMapping = classRequestMapping;
        this.beanName = beanName;
        this.beanType = beanType;
        this.methodRequestMapping = methodRequestMapping;
        this.method = method;
    }

    public boolean match(HttpServletRequest request) {
        //1 path 不需要进行匹配

        //2 匹配http Method
        if (this.matchHttpMethod(request)) {
            return false;
        }

        //3 匹配headers
        if (!this.matchHeaders(request)) {
            return false;
        }

        //3 匹配params
        if (!this.matchParams(request)) {
            return false;
        }

        return true;
    }


    private boolean matchHttpMethod(HttpServletRequest request) {
        if (this.methodRequestMapping.method().length > 0) {
            boolean methodMatch = false;
            String m = request.getMethod();
            for (RequestMethod rm : this.methodRequestMapping.method()) {
                if (rm.name().equals(m)) {
                    methodMatch = true;
                    break;
                }
            }
            return methodMatch;
        } else if (this.classRequestMapping != null && this.classRequestMapping.method().length > 0) {
            boolean methodMatch = false;
            String m = request.getMethod();
            for (RequestMethod rm : this.methodRequestMapping.method()) {
                if (rm.name().equals(m)) {
                    methodMatch = true;
                    break;
                }
            }
            return methodMatch;
        }
        return true;
    }


    private boolean matchHeaders(HttpServletRequest request) {
        if (this.methodRequestMapping.headers().length > 0) {
            for (String h : this.methodRequestMapping.headers()) {
                if (request.getHeader(h) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean matchParams(HttpServletRequest request) {
        if (this.methodRequestMapping.params().length > 0) {
            for (String p : methodRequestMapping.params()) {
                if (request.getParameter(p) != null) {
                    return false;
                }
            }
        } else if (this.classRequestMapping != null && classRequestMapping.params().length > 0) {
            for (String p : methodRequestMapping.params()) {
                if (request.getParameter(p) != null) {
                    return false;
                }
            }
        }

        return true;
    }

}
