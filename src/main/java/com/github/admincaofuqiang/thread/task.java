package com.github.admincaofuqiang.thread;

import java.util.concurrent.Callable;

public class task implements Callable<Object> {
    private  String threadName;
    public task(String threadName) {
        this.threadName=threadName;
    }

    @Override
    public Object call() throws Exception {
        Thread.currentThread().setName(threadName);
        Thread.sleep(1000);
        return Thread.currentThread().getName() + Thread.currentThread().getId();
    }
}
