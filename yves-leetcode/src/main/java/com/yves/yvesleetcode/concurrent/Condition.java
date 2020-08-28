package com.yves.yvesleetcode.concurrent;

import java.util.concurrent.TimeUnit;

public interface Condition {
    void await() throws InterruptedException;

    boolean await(long time, TimeUnit unit) throws InterruptedException;

    void signal();
}
