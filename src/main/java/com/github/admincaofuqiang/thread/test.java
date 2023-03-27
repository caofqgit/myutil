package com.github.admincaofuqiang.thread;

import org.omg.CORBA.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class test {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Callable<Object>> callables = new ArrayList<>();
        for(int i=0;i<10;i++){
            callables.add(new task("线程名称自定义--"+i+"--"));
        }
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<Object>> futures = executorService.invokeAll(callables);
        for (Future<Object> future : futures) {
            String s = future.get().toString();
            System.out.println(s);
        }
        executorService.shutdown();

    }
}
