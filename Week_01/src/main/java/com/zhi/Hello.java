package com.zhi;

/**
 * @Description 写一个简单的 Hello.java，里面需要涉及基本类型，四则运行，if 和 for，
 * 然后自己分析一下对应的字节码
 * @Author WenZhiLuo
 * @Date 2020-10-18 19:53
 */
public class Hello {
    //基本数据类型和四则运算
    public int calc() {
        int a = 100;
        int b = 200;
        int c = 300;
        return (a + b) * c;
    }

    public static void main(String[] args) {
        int tmp = 1;
        //if和for
        if (tmp == 1) {
            for (int i = 0; i < 2; i++) {
                System.out.println("hello");
            }
        }
    }
}
