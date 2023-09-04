package com.atguigu.springcloud.test;

import com.alibaba.fastjson.JSON;
import com.atguigu.springcloud.NginxTestApplication;
import com.atguigu.springcloud.entities.FloorsNode;
import com.atguigu.springcloud.entities.FloorsRow;
import com.atguigu.springcloud.enumObject.GameNodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NginxTestApplication.class)
@Slf4j
public class GameTreeTest {
    @Resource
    FloorsNode floorsNode;
    @Resource
    FloorsRow floorsRow;

    @Test
    public void test(){
        //第一层的最大节点数 最小节点数
        Integer firstFloorMini =2;
        Integer firstFloorMax =4;
        //中间层最大节点和最小节点
        Integer centerFloorsMini = 3;
        Integer centerFloorsMax = 5;
        //终点楼层
        Integer endFloorsMini = 2;
        Integer endFloorsMax = 3;
        //如果我需要10层的节点树
        List<Integer> floorsList = new ArrayList<>();
        List<FloorsRow> rows = new ArrayList<>();
        Integer numberFloor = 10;

        for(int i=0;(numberFloor-1)>i;i++){
            //现在的层数
            Integer nowFloor = i;
            nowFloor=nowFloor++;
            //起点楼层
           if(nowFloor==1){
               Integer num =  this.randomNumber(firstFloorMini,firstFloorMax);
               floorsList.add(num);
               floorsRow.addNodeList(num,nowFloor,null);
           }//中间楼层
           else if(i<numberFloor-2&&i<numberFloor-1){
               Integer num =  this.randomNumber(centerFloorsMini,centerFloorsMax);
               floorsList.add(num);
               floorsRow.addNodeList(num,nowFloor,floorsList.get(floorsList.size()-1));
           }//结束楼层
           else if(numberFloor-1==i){
               Integer num =  this.randomNumber(endFloorsMini,endFloorsMax);
               floorsList.add(num);
               floorsRow.addNodeList(num,nowFloor,floorsList.size()-1);
           }

        }




    }


    public Integer randomNumber(Integer mini,Integer max){
        // 创建一个Random对象
        int randomNum = ThreadLocalRandom.current().nextInt(mini, max);
        return randomNum;
    }

    //按照短的算
    //
    @Test
    public void test1(){
        List<Integer> a = Arrays.asList(1,2,3,4,5);
        List<Integer> b = Arrays.asList(1,2,3,4,5);
        List<String> path = new ArrayList<>();
        if(a.size()==b.size()){
            int randomMax = 0;
            Boolean flag = true;
        for(int i=0;a.size()>i;i++){
            int randomNum =1;
            //分叉数按照小的长度来
            Integer branchNum = this.branchNum(a.size());
            //极限路径只有一次
            randomNum = flagFunction(flag,branchNum);
            int flagNum =0;
            //是否往前一个元素分叉
            if(i>0&&a.size()-1>i){
                flagNum = ThreadLocalRandom.current().nextInt(0, 1);
            }

            if(branchNum>1){
                int calculate = branchNum;
                Set<Integer> set = new TreeSet<>();
                for(int s=0;calculate >s;calculate-- ){
                    set.add(calculate);
                }

                path.add(StringUtils.join(set,","));
            }

            path.add(randomNum+flagNum+"");
        }
        System.out.println(JSON.toJSON(path));
        }




    }
    //随机分支的最大值
    public Integer branchNum(int size){
        if(size==1||size==2){
            return size;
        }
        if(size==3||size==4){
            return 2;
        }
        if(size==5||size==6){
            return 3;
        }
        return 3;
    }

    public Integer flagFunction(Boolean flag,Integer branchNum){
        Integer randomNum = 0;
        if(flag) {
            //极限值只会出现一次
            randomNum = ThreadLocalRandom.current().nextInt(1, branchNum);
        }else{
            randomNum = ThreadLocalRandom.current().nextInt(1, branchNum-1);
        }
        //极限值只会出现一次
        if(branchNum==branchNum){
            flag = false;
        }
        return  randomNum;
    }
}
