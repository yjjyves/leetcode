package com.yves.mvc.mvc;

import lombok.Data;

import java.util.Map;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/15-11:50
 */
@Data
public class ModelAndView {
    private Map<String, Object> model;

    private Object view;

    private HttpStatus status;
}
