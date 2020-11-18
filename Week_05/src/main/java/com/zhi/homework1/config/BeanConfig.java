package com.zhi.homework1.config;
import com.zhi.homework1.pojo.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-11-18 19:19
 */
@Configuration
public class BeanConfig {

    @Bean("zhi")
    public Student newStu(){
        Student student = new Student();
        student.setId("11123");
        student.setName("小马快跑");
        return student;
    }
}
