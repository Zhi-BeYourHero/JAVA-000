package com.zhi.homework2;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description
 * 作业描述: 自定义一个 Classloader，加载一个 Hello.xlass(之所以改名为xclass是因为idea中不能自己创建class文件?) 文件，
 * 执行 hello 方法，此文件内容是一个 Hello.class 文件所有字节（x=255-x）(每个字节进行了处理,每个字节通过x = 255-x进行解密)处理后的文件。
 * @Author WenZhiLuo
 * @Date 2020-10-16 15:36
 */
public class HelloClassLoader extends ClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        HelloClassLoader helloClassLoader = new HelloClassLoader();
        //加载对应的类
        Class<?> helloClass = helloClassLoader.findClass("Hello");
        Method helloMethod = helloClass.getMethod("hello");
        helloMethod.invoke(helloClass.newInstance());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //1.通过类对象获取物理路径, 根据URL获取路径
        File file = new File(this.getClass().getResource("Hello.xlass").getPath());
        int length = (int)file.length();
        byte[] res = new byte[length];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < length; i++) {
            res[i] = (byte)(255 - res[i]);
        }
        return defineClass(name, res, 0, length);
    }
}