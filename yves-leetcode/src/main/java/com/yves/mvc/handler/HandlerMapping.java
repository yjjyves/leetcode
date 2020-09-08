package com.yves.mvc.handler;


import org.springframework.core.Ordered;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/15-15:01
 */
public interface HandlerMapping extends Ordered {
    Object getHandler(HttpServletRequest request) throws Exception;

    @Override
    default int getOrder() {
        return 0;
    }
}
