package com.atguigu.springcloud.test;

import com.alibaba.fastjson.JSON;
import com.atguigu.springcloud.service.util.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootTest
@Slf4j
class NginxTestApplicationTests {


    @Test
    void contextLoads() {
        //按照大集合的去进行路径生成
        //按照小集合的去进行路径分叉条数的上限控制

        //需要进行二次的线路优化
        //就是说如果轮询下来会出现 链接到的元素会比实际的元素多
        //即上一层有5个元素 下一层有5个元素 但是生成路径时会有分叉所以会出现 过饱和状态
        //{1,2}{2,3}{4}{5}{5,6}这种情况 所以
        //需要进行检查
        //


        List<Integer> a = Arrays.asList(1,2,3,4,5);
        List<Integer> b = Arrays.asList(1,2,3,4,5);
        List<String> path = new ArrayList<>();
        if(a.size()==b.size()) {
            int randomMax = 0;
            Boolean flag = true;
            Integer branchCount = 0;

            for (int i = 0; i<a.size() ; i++) {
                //int randomNum = 1;
                //分叉数按照小的长度来
                Integer branchMaxNum = this.branchNum(a.size());
                log.info("分叉数的最大"+branchMaxNum);
                //极限路径只有一次

                Integer randomNum = 0;
                //还需要进行判断 差值这个地方就别进行路径分支了
                if(flag) {
                    //极限值只会出现一次

                    randomNum = this.Random(1, branchMaxNum);
                }else{
                    randomNum = this.Random(1, branchMaxNum-1);
                }
                log.info("随机分叉数randomNum:{}",randomNum);
                //极限值只会出现一次
                if(branchMaxNum.equals(randomNum)){
                    flag = false;
                }

                Boolean flagNext = false;

                //是否往前一个元素分叉 第一个节点没有所谓前一个元素节点分叉
                if (i > 0 /*&& a.size()-1==i */) {

                    flagNext =RandomBoolean();
                }
                log.info("是否往前一个元素分叉:{}",flagNext);
                //分叉大于一
                int calculate = randomNum;
                //当前分指数+之前的分指数
                branchCount = randomNum +branchCount;
                if (calculate == 1) {
                    if(!flagNext){
                        //分叉等于一 不往前
                        path.add(branchCount+"");
                    }else{
                        //分叉等于一 往前
                        path.add(--branchCount+"");
                    }
                }else{
                    Set<Integer> set = new TreeSet<>();
                    if(flagNext){
                        --branchCount;
                    }
                    //总路径数是几
                    //c =当前路径数  branchCount 总路径数
                    int c= branchCount;
                    //之前的总路径数
                    int setc = branchCount -calculate;
                    //当前路径数>之前的总路径数;当前路径数--
                    for (;c>setc;c--) {

                        set.add(c);
                    }
                    path.add(StringUtils.join(set, ","));
                    //flagNext==true -1

                }
                //

            }
            log.info(""+JSON.toJSON(path));
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

    public Integer Random(int mini,int max ){
        Random rand =new Random();
        int i=rand.nextInt(max - mini + 1) + mini;
        return i;
    }
    public Boolean RandomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }


    @Test
    void Base64() {
        String Base64In = Base64Util.getBase64Encoder("你好你好，Base64");
        log.info("getBase64Encoder:{}",Base64In);
        String Base64Out =  Base64Util.getBase64Decoder(Base64In);
        log.info("getBase64Decoder:{}",Base64Out);
    }
}
