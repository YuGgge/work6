package com.zhang.config;

import com.zhang.aspect.ControlAspect;
import com.zhang.core.ControlProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableConfigurationProperties(ControlProperties.class)
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.zhang"})
public class FrequencyControlAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ControlAspect.class)
    public ControlAspect ControlAspect() {
        return new ControlAspect();
    }
}
