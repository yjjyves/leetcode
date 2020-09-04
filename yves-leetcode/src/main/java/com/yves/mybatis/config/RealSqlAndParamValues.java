package com.yves.mybatis.config;

import lombok.Data;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/16-11:37
 */
@Data
public class RealSqlAndParamValues {
    private String sql;

    private Object[] paramValues;

    public RealSqlAndParamValues(String sql, Object[] paramValues) {
        this.sql = sql;
        this.paramValues = paramValues;
    }
}
