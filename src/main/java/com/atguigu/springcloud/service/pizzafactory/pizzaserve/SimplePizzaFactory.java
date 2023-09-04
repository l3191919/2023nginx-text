package com.atguigu.springcloud.service.pizzafactory.pizzaserve;


import com.atguigu.springcloud.service.pizzafactory.pizzaserve.pizzatype.CheesePizza;
import com.atguigu.springcloud.service.pizzafactory.pizzaserve.pizzatype.Pizza;
import com.atguigu.springcloud.service.pizzafactory.pizzaserve.pizzatype.VeggiePizza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimplePizzaFactory {
    @Autowired
    private CheesePizza cheesePizza;
    @Autowired
    private VeggiePizza veggiePizza;

    public Pizza createPizza(String type){
        Pizza p = null;

        if("cheese".equals(type)){
            p = cheesePizza;
        }else if("veggie".equals(type)){
            p = veggiePizza;

        }
        return p;
    }

}
