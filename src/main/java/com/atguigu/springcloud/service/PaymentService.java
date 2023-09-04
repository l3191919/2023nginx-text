package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entities.Payment;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;

public interface PaymentService {
    public int create(Payment payment);

    public Payment getPaymentById(Long id);
    public IPage<Payment> getPaymentByCompanyId(Long companyId);
    public IPage<Payment> getPaymentByOr(HashMap<String, String> map);
    public Integer batchSavePayment();
//    public void getMethod();
//    public void go();
//    public void sendMessage();
}
