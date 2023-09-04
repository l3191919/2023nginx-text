package com.atguigu.springcloud.service.access;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ContextStore {
    public static final Integer MAX_QUEUE_SIZE = 1000;
    private ContextStore(){
    }
    public static final BlockingQueue<AccessLogDto> ACCESS_LOG_QUEUE = new LinkedBlockingQueue<AccessLogDto>(ContextStore.MAX_QUEUE_SIZE);
    public static Boolean addAccessLog(AccessLogDto log){
        return ACCESS_LOG_QUEUE.offer(log);
    }
}
