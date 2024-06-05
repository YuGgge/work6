package com.zhang;

import com.zhang.config.RedisConfig;
import com.zhang.exception.ExceptionAdvice;
import com.zhang.interceptors.UserInfoInterceptor;
import com.zhang.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.net.UnknownHostException;

@Configuration
@EnableAspectJAutoProxy
@EnableHystrix
@ComponentScan(basePackages = {"com.zhang"})
public class CommonAutoConfiguration {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private RedisConnectionFactory redisConnectionFactory;
    @Resource
    private RedisConfig redisConfig;
    @Bean
    @ConditionalOnMissingBean(RedisUtil.class)
    public RedisUtil RedisUtil() throws UnknownHostException {
        return new RedisUtil(redisConfig.redisTemplate(redisConnectionFactory));
    }



    @Bean
    @ConditionalOnMissingBean(RedisConfig.class)
    public RedisConfig RedisConfig() {
        return new RedisConfig();
    }
    @Bean
    @ConditionalOnMissingBean(UserContext.class)
    public UserContext UserContext() {
        return new UserContext();
    }
    @Bean
    @ConditionalOnMissingBean(JwtUtils.class)
    public JwtUtils JwtUtils() {
        return new JwtUtils();
    }
    @Bean
    @ConditionalOnMissingBean(FileUtils.class)
    public FileUtils FileUtils() {
        return new FileUtils();
    }
    @Bean
    @ConditionalOnMissingBean(DataUtils.class)
    public DataUtils DataUtils() {
        return new DataUtils();
    }
    @Bean
    @ConditionalOnMissingBean(UserInfoInterceptor.class)
    public UserInfoInterceptor UserInfoInterceptor() {
        return new UserInfoInterceptor();
    }
    @Bean
    @ConditionalOnMissingBean(ExceptionAdvice.class)
    public ExceptionAdvice ExceptionAdvice() {
        return new ExceptionAdvice();
    }

    @Bean
    @ConditionalOnMissingBean(TimeChangeUtils.class)
    public TimeChangeUtils TimeChangeUtils() {
        return new TimeChangeUtils();
    }

}
