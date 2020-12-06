package com.zhi.datasource.routing.aop;

import com.zhi.datasource.routing.annotation.DynamicDS;
import com.zhi.datasource.routing.annotation.ReadOnly;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 指定库或者指定从库负载均衡
 * @Author WenZhiLuo
 * @Date 2020-12-06 15:58
 */
@Configuration
public class DynamicDSInterceptorConfig {
    @Bean
    public DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor(){
        return new DynamicDSAnnotationInterceptor();
    }

    @Bean
    public ReadOnlyAnnotationInterceptor readOnlyAnnotationInterceptor(){
        return new ReadOnlyAnnotationInterceptor();
    }

    @Bean
    public Advisor dynamicDSAnnotationAdvisor(DynamicDSAnnotationInterceptor dynamicDSAnnotationInterceptor) {
        // 定义注解切点
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, DynamicDS.class);
        DefaultPointcutAdvisor dynamicDSAnnotationAdvisor = new DefaultPointcutAdvisor();
        //碰到指定切点就调用通知
        dynamicDSAnnotationAdvisor.setPointcut(pointcut);
        dynamicDSAnnotationAdvisor.setAdvice(dynamicDSAnnotationInterceptor);
        return dynamicDSAnnotationAdvisor;
    }

    @Bean
    public Advisor readOnlyAnnotationAdvisor(ReadOnlyAnnotationInterceptor readOnlyAnnotationInterceptor) {
        AnnotationMatchingPointcut readOnlyPointCut = new AnnotationMatchingPointcut(null, ReadOnly.class);
        DefaultPointcutAdvisor readOnlyAdvisor = new DefaultPointcutAdvisor();
        readOnlyAdvisor.setPointcut(readOnlyPointCut);
        readOnlyAdvisor.setAdvice(readOnlyAnnotationInterceptor);
        return readOnlyAdvisor;
    }
}