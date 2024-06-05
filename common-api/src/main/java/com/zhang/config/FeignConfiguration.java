package com.zhang.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author zhang
 * @date 2024/5/8
 * @Description
 */
@Configuration
public class FeignConfiguration implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 从header获取X-token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes attr = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = attr.getRequest();
        //获得请求携带的token
        String userId = request.getHeader("userId");
        if (StringUtils.hasText(userId)) {
            template.header("userId", userId);
        }
    }

}



