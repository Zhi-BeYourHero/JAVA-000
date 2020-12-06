package com.zhi.datasource.routing.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicDS {

    /**
     * 数据源标识
     *
     * @return 数据源标识
     */
    String value();
}