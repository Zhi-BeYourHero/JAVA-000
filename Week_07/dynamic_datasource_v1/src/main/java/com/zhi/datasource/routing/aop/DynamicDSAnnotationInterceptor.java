package com.zhi.datasource.routing.aop;

import com.zhi.datasource.routing.DynamicDSContextHolder;
import com.zhi.datasource.routing.annotation.DynamicDS;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description 动态数据源注解拦截器
 * @Author WenZhiLuo
 * @Date 2020-12-06 15:58
 */
public class DynamicDSAnnotationInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String dsFlag = invocation.getMethod().getAnnotation(DynamicDS.class).value();
            DynamicDSContextHolder.push(dsFlag);
            return invocation.proceed();
        } finally {
            if (DynamicDSContextHolder.empty()) {
                DynamicDSContextHolder.remove();
            } else {
                DynamicDSContextHolder.poll();
            }
        }
    }
}