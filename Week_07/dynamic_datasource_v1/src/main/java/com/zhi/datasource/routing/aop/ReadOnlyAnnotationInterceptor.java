package com.zhi.datasource.routing.aop;

import com.zhi.datasource.routing.DataSourceType;
import com.zhi.datasource.routing.DynamicDSContextHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Description
 * @Author WenZhiLuo
 * @Date 2020-12-06 17:22
 */
public class ReadOnlyAnnotationInterceptor implements MethodInterceptor {
    /**
     * 随机负载均衡
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            String secondary = DataSourceType.SECONDARY;
            String thirdly = DataSourceType.THIRDLY;
            // 0 - 9
            int val = (int)(Math.random() * 10);
            if((val & 1) == 0){
                DynamicDSContextHolder.push(secondary);
            }else{
                DynamicDSContextHolder.push(thirdly);
            }
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