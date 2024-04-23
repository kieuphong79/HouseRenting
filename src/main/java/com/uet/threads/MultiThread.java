package com.uet.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javafx.concurrent.Task;

public class MultiThread {
    private static ExecutorService ex = Executors.newFixedThreadPool(5, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = Executors.defaultThreadFactory().newThread(r);
            t.setDaemon(true);
            return t;
        }
    });
    public static <T> void execute(Task<T> task) {
        ex.execute(task);
    }
}
