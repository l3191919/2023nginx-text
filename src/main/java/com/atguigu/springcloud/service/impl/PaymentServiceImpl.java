package com.atguigu.springcloud.service.impl;


import com.atguigu.springcloud.dao.PaymentDao;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;
import com.atguigu.springcloud.service.thread.ThreadSavePaymentTask;
import com.atguigu.springcloud.service.util.ExecutorUtils;
import com.atguigu.springcloud.service.util.ThreadSaveUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static sun.swing.SwingUtilities2.submit;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Resource
    PaymentDao paymentDao;

    @Override
    public int create(Payment payment){
        return paymentDao.create(payment);
    }
    @Override
    public Payment getPaymentById(Long id){
        return paymentDao.getPaymentById(id);
    }
    @Override
    public IPage<Payment> getPaymentByCompanyId (Long companyId){
        IPage<Payment> page = paymentDao.getPaymentByCompanyId(new Page<>(1,5),companyId);
        System.out.println("总页数： " + page.getPages());
        System.out.println("总记录数： " + page.getTotal());
        page.getRecords().forEach(System.out::println);

        return page;
    }

    @Override
    public IPage<Payment> getPaymentByOr (HashMap<String,String> map){
        IPage<Payment> page = paymentDao.getPaymentByOr(new Page<>(1,5),map);
        System.out.println("总页数： " + page.getPages());
        System.out.println("总记录数： " + page.getTotal());
        page.getRecords().forEach(System.out::println);

        return page;
    }
    @PostConstruct
    public void getMethod(){


        System.out.println("aaaaa");
    }

    public Integer batchSavePayment(){
        CompletionService<AtomicInteger> cs = new ExecutorCompletionService<>(ExecutorUtils.getThreadPoolExecutor());
        final Integer groupNum= 100;

        List<Payment> payments =  this.addList();

        if(payments.size()==0){
            return -1;
        }
        //每份多少个
        Integer num = payments.size()/groupNum;
        //余数
        Integer num2 = payments.size()%groupNum;

        Integer succeedSum = 0;
        CopyOnWriteArrayList<Payment> paymentList = new CopyOnWriteArrayList<>();
        Map<String,CopyOnWriteArrayList<Payment>> map = new HashMap<>(10);



        int key = 1;
        for (int i= 0;payments.size()>i;i++){

            int k =i+1;
            paymentList.add(payments.get(i));
            //每一组为num
            //10*num-1则是不让最后余出来的数单独开线程
            //||
            //把最后余出来的数添加到最后一个线程中
        if((k%groupNum==0&&i<groupNum*num-1)||(i>groupNum*num&&k==payments.size())){

            CopyOnWriteArrayList<Payment> mapList = new CopyOnWriteArrayList<>();
            mapList.addAll(paymentList);
            map.put("thread"+(key++),mapList);
            //清空
            paymentList.clear();
        }
        }
        List<Future<AtomicInteger>> threadIntList = new ArrayList<>();
        for(Map.Entry<String,CopyOnWriteArrayList<Payment>> mapPayments:map.entrySet()){
            Future<AtomicInteger> succeed = cs.submit(new ThreadSavePaymentTask(mapPayments.getValue(),this));
            threadIntList.add(succeed);
        }
        try {
            for(Future<AtomicInteger> atomicIntegerFuture:threadIntList){
                //get方法会阻塞(单独拿出来)
                succeedSum = succeedSum+Integer.valueOf(atomicIntegerFuture.get().toString());
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        log.info("总共成功数:{}",succeedSum);
        return succeedSum;
    }
    //模仿从日志表拿出不同条数的数据
    public List<Payment> addList(){
        List<Payment> payments = new ArrayList<>();
        for(int i = 0;730>i;i++){
            Long companyId =Long.valueOf(i+100) ;
            Payment payment = Payment.builder().companyId(companyId).serial("你好"+i).build();
            payments.add(payment);
        }
        return  payments;
    }
}
