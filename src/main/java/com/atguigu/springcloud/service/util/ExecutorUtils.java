package com.atguigu.springcloud.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ExecutorUtils {


    public final static ThreadPoolExecutor threadPoolExecutor;


    static {
        int processors = 64;
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
        log.info("corePoolSize:{}", corePoolSize);
        corePoolSize = corePoolSize > processors ? corePoolSize : processors;
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, corePoolSize,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10000),
                (r, executor) -> {
                    try {
                        executor.getQueue().put(r);
                    } catch (InterruptedException e) {
                        log.warn("retry put task error,message:{}", e);
                    }
                });

    }


    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

}
