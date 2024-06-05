package com.zhang.client;

import com.zhang.entity.Video;
import com.zhang.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhang
 * @date 2024/5/12
 * @Description
 */
@FeignClient("video-service")
public interface VideoClient {

    @GetMapping("/video/selectById")
    Result<Video> selectById(@RequestParam("videoId") String id);

    @PostMapping("/video/updateById")
    Result updateById(@RequestParam("video") Video video);
}
