package com.zhang.client;

import com.zhang.entity.Comment;
import com.zhang.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhang
 * @date 2024/5/12
 * @Description
 */
@FeignClient("comment-service")
public interface CommentClient {
    @PostMapping("/comment/updateById")
    Result updateById(@RequestParam("comment") Comment comment);
}
