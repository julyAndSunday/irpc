//package com.starter.irpc;
//
//import com.starter.irpc.annotation.IRpc;
//import com.starter.irpc.annotation.IRpcAutoWired;
//import org.reflections.Reflections;
//import org.reflections.scanners.FieldAnnotationsScanner;
//
//import java.io.File;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.util.Set;
//
///**
// * @Description:
// * @Author: July
// * @Date: 2021-08-12 22:04
// **/
//public class Main {
//    public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, InstantiationException {
//        String property = System.getProperty("user.dir");
//        System.out.println(property);
//        File file = new File(property);
//        Reflections reflections = new Reflections("com.starter");
//        Set<Class<?>> set = reflections.getTypesAnnotatedWith(IRpc.class);
//        System.out.println(set.size());
//    }
//
//    public static String getRealPath() {
////通过类加载器获取jar包的绝对路径
//        String realPath = Reflections.class.getClassLoader().getResource("")
//                .getFile();
//        java.io.File file = new java.io.File(realPath);
//        realPath = file.getParentFile().getAbsolutePath(); //获取jar包的上级目录
//
//        try {
////路径decode转码
//            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return realPath;
//
//    }
//}
