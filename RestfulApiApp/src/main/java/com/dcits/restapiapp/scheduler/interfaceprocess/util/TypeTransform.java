package com.dcits.restapiapp.scheduler.interfaceprocess.util;


/**
 * java 类型转换,将value值按照type进行转换
 */
public class TypeTransform {

    public static Object typeTransform(Object value,String type)throws Exception{
        String typeUpperCase = type.trim().toUpperCase();
        if(typeUpperCase.equals("INT")){
            return Integer.valueOf(value.toString());
        }else if(typeUpperCase.equals("DOUBLE")){
            return Double.valueOf(value.toString());
        }else if(typeUpperCase.equals("Long")){
            return Long.valueOf(value.toString());
        }else if(typeUpperCase.equals("BOOLEAN")){
            return Boolean.valueOf(value.toString());
        }else if(typeUpperCase.equals("STRING")){
            return value.toString();
        }
        return null;

    }
}
