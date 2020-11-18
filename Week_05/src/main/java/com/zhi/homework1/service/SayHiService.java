package com.zhi.homework1.service;

import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-11-18 19:26
 */
@Service("hi")
public class SayHiService {
    public void sayHi(){
        System.out.println("hi...");
    }
}
