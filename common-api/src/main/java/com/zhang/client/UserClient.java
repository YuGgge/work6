package com.zhang.client;

import com.zhang.entity.User;
import com.zhang.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhang
 * @date 2024/5/12
 * @Description
 */
@FeignClient("user-service")
public interface UserClient {
    @GetMapping("/user/loadUserByUsername")
    Result<User> loadUserByUsername(@RequestParam("username") String username);
}
