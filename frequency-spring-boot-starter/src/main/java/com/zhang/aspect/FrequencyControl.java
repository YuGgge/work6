package com.zhang.aspect;



import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 自定义限流注解
 *
 * @author 31445
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FrequencyControl {
    /**
     *  标识前缀key
     */
    String prefix() default "";

    /**
     *  频控控制时间范围
     */
    int timeRange()default 0;

    /**
     *  频控控制时间单位  默认为秒
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     *  单位频控时间范围内最大访问次数
     */
    int maxCount() default 0;
}
