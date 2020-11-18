package com.zhi.homework2.config;

import com.zhi.homework2.pojo.Klass;
import com.zhi.homework2.pojo.School;
import com.zhi.homework2.pojo.Student;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-11-18 19:49
 */
@Configuration
@ConditionalOnProperty(
        value = "zhi.enable",
        havingValue = "true",
        matchIfMissing = true
)
public class ZhiConfiguration {
    @Bean("student100")
    public Student student(){
        return new Student(111, "student100");
    }

    @Bean
    public Klass klass(){
        return new Klass();
    }

    /**
     * 确保School加载时可以依赖注入student,kclass
     */
    @Bean
    @ConditionalOnBean(name = {"student100", "klass"})
    public School school(){
        return new School();
    }
}