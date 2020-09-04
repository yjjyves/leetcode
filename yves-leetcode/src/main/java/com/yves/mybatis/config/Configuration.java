package com.yves.mybatis.config;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/6/16-11:04
 */
@Data
public class Configuration {

    private Map<String, MapperStatement> mapperStatements = new ConcurrentHashMap<>();

    private XMLMapperBuilder xmlMapperBuilder;

    private MapperAnnotationBuilder mapperAnnotationBuilder;


    public void addMapperStatement(String id, MapperStatement mapperStatement) {
        mapperStatements.put(id, mapperStatement);
    }

    public MapperStatement getMapperStatement(String id) {
        return mapperStatements.get(id);
    }

    public boolean hasMapperStatement(String id) {
        return mapperStatements.containsKey(id);
    }
}
