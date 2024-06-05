package com.zhang.client;

import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author zhang
 * @date 2024/5/20
 * @Description
 */
@FeignClient("follow-service")
public interface FollowClient {

}

