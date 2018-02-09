package com.jone.smoke.entity.common;

public class ResultUtil {

    public static BaseData success(){
        BaseData baseData = new BaseData();
        baseData.setCode(0);
        return baseData;
    }

    public static BaseData success(Object object){
        BaseData baseData = new BaseData();
        baseData.setCode(0);
        baseData.setData(object);
        return baseData;
    }

    public static BaseData success(Object object,String message){
        BaseData baseData = new BaseData();
        baseData.setCode(0);
        baseData.setData(object);
        baseData.setMessage(message);
        return baseData;
    }

    public static BaseData success(String message){
        BaseData baseData = new BaseData();
        baseData.setCode(0);
        baseData.setMessage(message);
        return baseData;
    }

    public static BaseData error(Integer code,String message){
        BaseData baseData = new BaseData();
        baseData.setCode(code);
        baseData.setMessage(message);
        return baseData;
    }
}
