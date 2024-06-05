package com.zhang.aspect;

import com.zhang.api.ControlManager;
import com.zhang.core.ControlProperties;
import com.zhang.core.ControlRule;
import com.zhang.exception.ControlException;
import com.zhang.utils.RedisUtil;
import com.zhang.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
@Slf4j
public class ControlAspect {
   // private final Logger logger = LoggerFactory.getLogger(ControlAspect.class);
    @Autowired
    private ControlProperties controlProperties;
    @Autowired
    private ControlManager controlManager;


    @Pointcut("@annotation(frequencyControl)")
    public void controllerAspect(FrequencyControl frequencyControl) {}


    @Around(value = "controllerAspect(frequencyControl)", argNames = "joinPoint,frequencyControl")
    public Object aroundRateLimit(ProceedingJoinPoint joinPoint, FrequencyControl frequencyControl) throws Throwable {
        if (!controlProperties.isEnabled()){
            // 如果拒绝执行频率控制，执行被切入的方法
            return joinPoint.proceed();
        }

        ControlRule controlRule = finishControlRule(frequencyControl);


        String ipAddress = WebUtil.getIpAddress();
        String uri = WebUtil.getUri();
        controlManager.controlStrategy(controlRule, uri, ipAddress);
        // 执行被切入的方法
        return joinPoint.proceed();
    }



    /**
     * 整理频率控制的参数
     * @param frequencyControl
     * @return
     */
    public ControlRule finishControlRule(FrequencyControl frequencyControl){
        // 获取注解中的属性值
        //参数可能为空，需要读取配置文件，如果都为空则用默认值
        ControlRule controlRule = ControlRule.builder()
                .timeRange(frequencyControl.timeRange())
                .timeUnit(frequencyControl.timeUnit())
                .maxCount(frequencyControl.maxCount())
                .build();

        //判断整理参数
        if (StringUtils.isBlank(frequencyControl.prefix())){
            controlRule.setPrefix(controlProperties.getControlRule().getPrefix());
        }
        if (frequencyControl.timeRange() <= 0){
            controlRule.setTimeRange(controlProperties.getControlRule().getTimeRange());
        }
        if (frequencyControl.timeUnit() == null){
            controlRule.setTimeUnit(controlProperties.getControlRule().getTimeUnit());
        }
        if (frequencyControl.maxCount() <= 0){
            controlRule.setMaxCount(controlProperties.getControlRule().getMaxCount());
        }
        return controlRule;
    }

}
