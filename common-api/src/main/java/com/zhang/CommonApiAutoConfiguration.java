package com.zhang;

import com.zhang.config.FeignConfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "com.zhang.client")
public class CommonApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FeignConfiguration.class)
    public FeignConfiguration FeignConfiguration() {
        return new FeignConfiguration();
    }


}
