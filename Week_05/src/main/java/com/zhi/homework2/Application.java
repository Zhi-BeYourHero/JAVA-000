package com.zhi.homework2;

import com.zhi.homework2.pojo.Klass;
import com.zhi.homework2.pojo.School;
import com.zhi.homework2.pojo.Student;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-11-18 20:01
 */
@EnableAutoConfiguration
public class Application implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        Student student100 = applicationContext.getBean("student100", Student.class);
        Klass kclass = applicationContext.getBean("klass", Klass.class);
        School school = applicationContext.getBean("school", School.class);
        System.out.println(student100);
        System.out.println(kclass);
        System.out.println(school);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
