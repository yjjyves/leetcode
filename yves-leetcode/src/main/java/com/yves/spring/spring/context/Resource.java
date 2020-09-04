package com.yves.spring.spring.context;

import java.io.File;

public interface Resource extends InputStreamSource {
    String CLASS_PATH_PREFIX = "classpath:";
    String FILE_PATH_PREFIX = "file:";

    boolean exists();

    boolean isReadable();

    boolean isOpen();

    File getFile();

}
