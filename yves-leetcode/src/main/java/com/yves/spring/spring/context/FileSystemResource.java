package com.yves.spring.spring.context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 功能描述
 *
 * @author yijinjin
 * @date 2020/5/27-15:31
 */
public class FileSystemResource implements Resource {
    private File file;

    public FileSystemResource(String fileName) {
        this.file = new File(fileName);
    }

    public FileSystemResource(File file) {
        super();
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public boolean exists() {
        return file == null ? false : file.exists();
    }

    @Override
    public boolean isReadable() {
        return file == null ? false : file.canRead();
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public File getFile() {
        return file;
    }
}
