package com.atguigu.springcloud.dao;

import com.atguigu.springcloud.entities.Payment;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;

@Mapper
public interface PaymentDao {
    /**
     * 新增
     *
     * @param payment
     * @return
     */
    int create(Payment payment);

    /**
     * 根据Id查询
     *
     * @param id
     * @return
     */
    Payment getPaymentById(@Param("id") Long id);

    /**
     * 根据companyId查询
     *
     * @param companyId
     * @return
     */
    IPage<Payment> getPaymentByCompanyId(Page<Payment> page, @Param("companyId") Long companyId);


    IPage<Payment> getPaymentByOr(Page<Payment> page, @Param("map") HashMap<String, String> map);
}
