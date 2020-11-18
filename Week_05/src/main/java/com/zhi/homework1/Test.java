package com.zhi.homework1;


import com.zhi.homework1.pojo.Student;
import com.zhi.homework1.service.SayHiService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-11-18 18:53
 */
public class Test {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Student stu1 = applicationContext.getBean("stu1", Student.class);
        Student stu2 = applicationContext.getBean("zhi",Student.class);
        SayHiService sayHiService = new SayHiService();
        sayHiService.sayHi();
        System.out.println(stu1);
        System.out.println(stu2);
    }
}