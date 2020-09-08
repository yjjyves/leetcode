package com.yves.mvc.handler;


import com.yves.mvc.mvc.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/15-14:52
 */
public class SimpleControllerHandlerAdapter implements HandlerAdapter {


    @Override
    public boolean supports(Object handler) {
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return null;
    }
}
