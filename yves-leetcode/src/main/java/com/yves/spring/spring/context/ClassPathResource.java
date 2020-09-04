package com.yves.spring.spring.context;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-16:26
 */
@Data
public class ClassPathResource implements Resource {
    private String path;

    private Class<?> clazz;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this.path = path;
    }

    public ClassPathResource(String path, Class<?> clazz) {
        this(path, clazz, null);
    }

    public ClassPathResource(String path, Class<?> clazz, ClassLoader classLoader) {
        super();
        this.path = path;
        this.clazz = clazz;
        this.classLoader = classLoader;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean isReadable() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (StringUtils.isNotBlank(path)) {
            if (this.clazz != null) {
                return this.clazz.getResourceAsStream(path);
            }

            if (this.classLoader != null) {
                return this.classLoader.getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);
            }

            return this.getClass().getResourceAsStream(path);
        }
        return null;
    }
}
