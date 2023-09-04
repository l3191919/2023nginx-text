package com.atguigu.springcloud.service.util;

import com.atguigu.springcloud.entities.Payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadSaveUtils {
    private static final ThreadLocal<List<Payment>> paymentThreadLocal = new ThreadLocal<>();

    public static ThreadLocal<List<Payment>> getPaymentThreadLocal() {
        return paymentThreadLocal;
    }

    public static void clearPaymentThreadLocalParameter() {
        getPaymentThreadLocal().remove();
    }


    /**
     * 添加本地线程参数
     *
     * @return
     */
    public static void addPaymentThreadLocalParameter(List<Payment> paymentList) {
        ThreadLocal<List<Payment>> threadLocal = getPaymentThreadLocal();
        threadLocal.set(paymentList);
    }

}
