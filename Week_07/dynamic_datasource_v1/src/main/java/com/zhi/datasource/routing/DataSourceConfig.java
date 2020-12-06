package com.zhi.datasource.routing;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description 动态数据源配置
 * @Author WenZhiLuo
 * @Date 2020-12-06 15:34
 */
@Configuration
public class DataSourceConfig {

    @Bean("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.primary")
    public DataSource primaryDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("secondary")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.secondary")
    public DataSource secondaryDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("thirdly")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.thirdly")
    public DataSource thirdlyDataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * Create specify map of target DataSources and configure DataSource
     *
     * @return DataSource
     */
    @Bean("dynamicRoutingDataSource")
    public DataSource dynamicRoutingDataSource() {
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        Map<Object, Object> dataSourceMap = new HashMap<>(3);
        dataSourceMap.put(DataSourceType.PRIMARY, primaryDataSource());
        dataSourceMap.put(DataSourceType.SECONDARY, secondaryDataSource());
        dataSourceMap.put(DataSourceType.THIRDLY, thirdlyDataSource());
        // 将 master 数据源作为默认指定的数据源
        dynamicRoutingDataSource.setDefaultTargetDataSource(primaryDataSource());
        // 设置指定数据源映射
        dynamicRoutingDataSource.setTargetDataSources(dataSourceMap);
        return dynamicRoutingDataSource;
    }
}