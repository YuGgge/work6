package com.zhang.filter;

import com.zhang.entity.User;
import com.zhang.exception.UserException;
import com.zhang.utils.JwtUtils;
import com.zhang.utils.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @author zhang
 * @date 2024/5/23
 * @Description
 */
@Component
@Slf4j
public class AuthGlobalFilter implements GlobalFilter, Ordered {
/*
    @Value("${auth.excludePaths}")
    private String excludePaths;*/


    @Autowired
    private RedisUtil redisUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        //获取accessToken
        String accessToken = request.getHeaders().getFirst("accessToken");

        //排除登录(login)+注册请求(register)请求，排除accessToken没有内容的
        if (!StringUtils.hasText(accessToken)
                || Objects.equals(request.getPath().toString(), "/user/login")
                ||Objects.equals(request.getPath().toString(), "/user/register")) {
            //放行
            return chain.filter(exchange);
        }
        //未登录异常
        if (Objects.equals(accessToken,"undefined")){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
            //throw new UserException("请先登录");
        }

        //检查token时效性
        if (JwtUtils.isTokenExpired(accessToken)){
            String refreshToken = request.getHeaders().getFirst("refreshToken");
            if (JwtUtils.isTokenExpired(refreshToken)){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
                //throw new UserException("过久未登录，请重新登录");
            }else {
                //利用refreshToken生成accessToken和refreshToken
                //将新Token存到Api fox的Header中
                try {
                    accessToken = JwtUtils.createAccessTokenByRefresh(refreshToken);
                    exchange.getAttributes().put("accessToken", accessToken);
                    String newRefreshToken=JwtUtils.createRefreshTokenByRefresh(refreshToken);
                    exchange.getAttributes().put("refreshToken",newRefreshToken);
                } catch (Exception e) {
                    log.error(String.valueOf(e));
                    throw new UserException("token非法");
                }
            }
        }

        //解析token
        String userId;
        try {
            Claims claims = JwtUtils.parseJWT(accessToken);
            userId = claims.getSubject();
        } catch (Exception e) {
            log.error(String.valueOf(e));
            throw new UserException("token非法");
        }

        //从redis中获取用户信息
        String redisKey = "login:" + userId;
        User user = (User) redisUtil.get(redisKey);
        if(Objects.isNull(user)){
            throw new UserException("用户未登录");
        }

        //将用户信息放入上下文
        ServerWebExchange serverWebExchange = exchange.mutate()
                .request(builder -> builder.header("userId", userId))
                .build();
        return chain.filter(serverWebExchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
