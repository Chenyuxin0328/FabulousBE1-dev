package com.creatorsn.fabulous.entity;

/**
 * 用户性别
 */
public enum UserGenderEnum {
    /**
     * 秘密
     */
    SECRET(0),
    /**
     * 男性
     */
    MALE(1),
    /**
     * 女性
     */
    FEMALE(2);

    final private int value;
    UserGenderEnum(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }

    public static UserGenderEnum fromValue(int value){
        for (UserGenderEnum type: UserGenderEnum.values()){
            if(type.value==value){
                return type;
            }
        }
        throw new IllegalArgumentException("Cannot create value from value: "+ value +"!");
    }
}
