package com.zhi.homework2.pojo;

import com.zhi.homework2.aop.ISchool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-11-18 19:45
 */
@Data
public class School implements ISchool {

    // Resource
    @Autowired(required = true) //primary
            Klass class1;

    @Resource(name = "student100")
    Student student100;

    @Override
    public void ding(){

        System.out.println("Class1 have " + this.class1.getStudents().size() + " students and one is " + this.student100);

    }
}
