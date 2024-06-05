package com.zhang.core;

import lombok.*;

import java.util.concurrent.TimeUnit;

/**
 *
 * 频率控制的规则
 * @author zhang
 * &#064;date  2024/4/29
 * &#064;Description
 */


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ControlRule {
    /**
     * 给定的时间范围,默认值
     */
    public static final int DEFAULT_TIMERANGE = 60;
    /**
     * 给定时间单位
     */
    public static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;

    /**
     * 一定时间内最多访问次数,默认值
     */
    public static final int DEFAULT_COUNT = 1;

    /**
     * 标识key
     */
    private String prefix;
    /**
     * 频控控制时间范围
     */
    private int timeRange;
    /**
     * 频控控制时间单位
     */
    private TimeUnit timeUnit;
    /**
     *单位频控时间范围内最大访问次数
     */
    private int maxCount;

    public ControlRule init(){
        this.setTimeRange(DEFAULT_TIMERANGE);
        this.setMaxCount(DEFAULT_COUNT);
        this.setTimeUnit(DEFAULT_TIMEUNIT);
        return this;
    }

}
