package com.atguigu.springcloud.service.thread;

import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import com.atguigu.springcloud.service.util.ThreadSaveUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
public class ThreadSavePaymentTask implements Callable<AtomicInteger> {
    private CopyOnWriteArrayList<Payment> paymentList = new CopyOnWriteArrayList<>();
    private PaymentService paymentService;
    public ThreadSavePaymentTask(CopyOnWriteArrayList<Payment> paymentList,PaymentService paymentService){
        this.paymentService =paymentService;
        this.paymentList.addAll(paymentList) ;
    }




    @Override
    public AtomicInteger call() throws InterruptedException {
        log.info(String.valueOf(this.paymentList.size()));
        ThreadLocal<List<Payment>> paymentThreadLocal = ThreadSaveUtils.getPaymentThreadLocal();
        ThreadSaveUtils.addPaymentThreadLocalParameter(this.paymentList);
        log.info(Thread.currentThread().getName()+"多线程中数据条数"+paymentThreadLocal.get().size());
        AtomicInteger integer = new AtomicInteger(0);
//        if(Thread.currentThread().getName().equals("pool-1-thread-1")||Thread.currentThread().getName().equals("pool-1-thread-5")){
//            log.info("停止10");
//            Thread.sleep(10000);
//        }
        for (Payment payment:paymentThreadLocal.get()) {
            try {
                paymentService.create(payment);
                integer.getAndIncrement();
            }catch (Exception e){
                //报错就对日志表写入插入错误(代码中没有这个东西)

                e.printStackTrace();
            }finally {

            }

        }
        ThreadSaveUtils.clearPaymentThreadLocalParameter();
        return integer;
    }
}
