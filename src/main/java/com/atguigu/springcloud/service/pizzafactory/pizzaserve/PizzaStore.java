package com.atguigu.springcloud.service.pizzafactory.pizzaserve;

import com.atguigu.springcloud.service.pizzafactory.pizzaserve.pizzatype.Pizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* 披萨仓库*/
@Service
public class PizzaStore {
    @Autowired
    private SimplePizzaFactory factory ;
//    public PizzaStore (SimplePizzaFactory factory){
//        this.factory = factory;
//    }

    public Pizza orderPizza(String type){
        Pizza pizza;
        pizza = factory.createPizza(type);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }

}
