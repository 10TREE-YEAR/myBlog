package com.wjl.blog.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @Description: 属性相同对象转换
* @Param:  
* @return:  
* @Author: wangjialu
* @Date: 2020/3/2 
*/ 
public class ConvertBeanUtils {

    /**
     * 将一个对象转换为另一个对象需要两个bean对象的变量相同
     *
     * @param <T1>      要转换的对象
     * @param <T2>      转换后的类
     * @param orimodel  要转换的对象
     * @param castClass 转换后的类
     * @return 转换后的对象
     */
    public static <T1, T2> T2 convertBean(T1 orimodel, Class<T2> castClass) {
        T2 returnModel;
        try {
            returnModel = castClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建" + castClass.getName() + "对象失败");
        }
        //要转换的字段集合
        List<Field> fieldlist = new ArrayList<Field>();
        final String objClass = "java.lang.object";
        //循环获取要转换的字段,包括父类的字段
        while (castClass != null && !objClass.equals(castClass.getName().toLowerCase())) {
            fieldlist.addAll(Arrays.asList(castClass.getDeclaredFields()));
            //得到父类,然后赋给自己
            castClass = (Class<T2>) castClass.getSuperclass();
        }
        for (Field field : fieldlist) {
            PropertyDescriptor getpd;
            PropertyDescriptor setpd;
            try {
                getpd = new PropertyDescriptor(field.getName(), orimodel.getClass());
                setpd = new PropertyDescriptor(field.getName(), returnModel.getClass());
            } catch (Exception e) {
                continue;
            }
            try {
                Method getMethod = getpd.getReadMethod();
                Object transValue = getMethod.invoke(orimodel);
                Method setMethod = setpd.getWriteMethod();
                setMethod.invoke(returnModel, transValue);
            } catch (Exception e) {
                throw new RuntimeException("cast " + orimodel.getClass().getName() + "to "
                        + castClass.getName() + " failed");
            }
        }
        return returnModel;
    }
}
