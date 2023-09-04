package com.atguigu.springcloud.enumObject;

public enum GameNodeEnum {
    BATTLE(1,"战斗","BATTLE"),
    TASK(2,"任务","TASK"),
    MATTER(3,"战斗","MATTER");
    private Integer value;
    private String name;
    private String type;


    GameNodeEnum(Integer value,String name,String type){
        this.name = name;
        this.type = type;
        this.value = value;
    }
    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public static GameNodeEnum getGameNodeEnum(Integer num) {
        for (GameNodeEnum game:values()){
            if (game.getValue().equals(num)) {
                return game;
            }
        }
        return BATTLE;
    }
}
