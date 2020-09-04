package com.yves.mybatis.config;

import lombok.Data;

import java.util.List;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/16-11:02
 */
@Data
public class MapperStatement {
    private String id;

    private String sql;

    private SqlCommandType sqlCommandType;

    private List<ParameterMap> parameterMaps;

    public RealSqlAndParamValues getRealSqlAndParamValues(Object[] args) {
        // TODO
        return null;
    }
}
