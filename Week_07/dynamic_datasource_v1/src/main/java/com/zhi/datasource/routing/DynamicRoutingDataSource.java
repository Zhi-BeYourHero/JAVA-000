package com.zhi.datasource.routing;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Description 数据源路由配置
 * @Author WenZhiLuo
 * @Date 2020-12-06 15:46
 */
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDSContextHolder.peek();
    }
}