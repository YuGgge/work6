package com.zhang.handle;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    /**
     * 网关中检查过token，所以这里不需要检查token直接放行。
     * @param request
     * @param response
     * @param filterChain
     * @throws javax.servlet.ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws javax.servlet.ServletException, IOException {
        filterChain.doFilter(request, response);
    }
}
