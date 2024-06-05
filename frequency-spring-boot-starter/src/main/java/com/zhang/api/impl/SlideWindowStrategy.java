package com.zhang.api.impl;

import com.zhang.api.ControlManager;
import com.zhang.core.ControlRule;
import com.zhang.exception.ControlException;
import com.zhang.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author zhang
 * @date 2024/5/22
 * @Description
 */
@Component
@Slf4j
public class SlideWindowStrategy implements ControlManager {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 频率控制策略
     * @param controlRule
     * @param uri
     * @param ipAddress
     * @return
     */
    @Override
    public void controlStrategy(ControlRule controlRule, String uri, String ipAddress) throws IOException {

        //定义key ,如果为空我们会以uri+ip作为key
        String key;
        if (StringUtils.isBlank(controlRule.getPrefix())){
            key = uri + "_" + ipAddress;
        }else{
            key = controlRule.getPrefix() + "_" + ipAddress;
        }

        /**
         * java代码操作redis
         */
/*        redisTemplate.opsForZSet().add(key ,String.valueOf(currentTimeMillis) , (int) currentTimeMillis);
        redisTemplate.expire(key, period, TimeUnit.SECONDS);
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, currentTimeMillis - period * 1000);
        Long count = redisTemplate.opsForZSet().zCard(key);


        if (count > controlRule.getMaxCount()){
            log.error("接口拦截：{} 请求超过限制频率【{}次/{}s】,IP为{}", uri, controlRule.getMaxCount(), period, ipAddress);
            throw new ControlException("请求过于频繁，请稍后重试");
        }*/
        /**
         * lua脚本操作redis
         */
        String LuaScript = loadLuaScript("classpath:script/SlideWindowStrategy.lua");
        RedisScript<Long> redisLuaScript = new DefaultRedisScript<>(LuaScript, Long.class);
        List<Object> keys = Collections.singletonList(key);
        //获得当前时间戳，单位为毫秒
        long currentTimeMillis = System.currentTimeMillis();
/*        Long count = (Long) redisTemplate.execute(
                redisLuaScript,
                keys,
                controlRule.getTimeRange(),
                currentTimeMillis
        );*/
        Long count = (Long) redisTemplate.execute(
                redisLuaScript, keys,
                controlRule.getMaxCount(),
                controlRule.getTimeRange(),
                currentTimeMillis);
        if (count == 0) {
            log.info("接口拦截：{} 请求超过限制频率【{}次/{}s】,IP为{}", uri, controlRule.getMaxCount(), controlRule.getTimeRange(), ipAddress);
            throw new ControlException("请求过于频繁，请稍后重试");
        }
    }

    /**
     * 加载lua脚本
     * @param path
     * @return
     * @throws IOException
     */
    private String loadLuaScript(String path) throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(this.getClass().getClassLoader());
        Resource[] resource = resolver.getResources(path);
        String luaScriptContent = StreamUtils.copyToString(resource[0].getInputStream(), StandardCharsets.UTF_8);
        return luaScriptContent;
    }

    private boolean isAllowed(RedisScript<Long> redisLuaScript, List<Object> key, int maxCount, int timeRange){
        System.out.println(maxCount);
        //获得当前时间戳，单位为毫秒
        long currentTimeMillis = System.currentTimeMillis();
        Long count = (Long) redisTemplate.execute(
                redisLuaScript,
                key,
                timeRange,
                currentTimeMillis
        );

        if (count == null){
            log.error("执行lua脚本失败");
        }
        return count <= maxCount;
    }
}
