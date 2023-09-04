package com.atguigu.springcloud.entities;

import com.atguigu.springcloud.enumObject.GameNodeEnum;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
//行对象
public class FloorsRow {
    //这是第几层
    Integer row;
    //这一层的元素
    List<FloorsNode> nodeList;

    //根据节点数 层数 生成对应的路径和对象
    public void addNodeList(Integer nodeNum,Integer row,Integer beforeNodeNum){

            for(int i=0;nodeNum<i;i++){
                FloorsNode floorsNode = new FloorsNode();
                int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
                floorsNode.setNodeName(GameNodeEnum.getGameNodeEnum(randomNum).getName());
                floorsNode.setNodeType(GameNodeEnum.getGameNodeEnum(randomNum).getType());
                nodeList.add(floorsNode);
            }
        if(row>1){
            //生成路径
            if(beforeNodeNum==nodeNum){

            }
            if(beforeNodeNum>nodeNum){

            }
            if(beforeNodeNum<nodeNum){

            }
        }
    }

}
