package com.atguigu.springcloud.entities;

import lombok.Data;

import java.util.List;

@Data
public class FloorsNode {
    //节点名称
    String nodeName;
    //节点类型
    String nodeType;
    //路径
    List<Integer> path;
}
