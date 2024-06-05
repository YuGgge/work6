package com.zhang.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import com.zhang.entity.User;
import com.zhang.exception.UserException;
import com.zhang.service.UserService;
import com.zhang.utils.FileUtils;
import com.zhang.utils.UserContext;
import com.zhang.vo.Result;
import com.zhang.vo.ResultWithMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * @author zhang
 * &#064;date  2024/2/10
 * &#064;Description  用户Controller层
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileUtils fileUtils;


    @GetMapping("/loadUserByUsername")
    public Result<User> loadUserByUsername(@RequestParam("username") String username){
        User user = (User) userService.loadUserByUsername(username);
        return Result.OK(user);
    }

    /**
     * 注册方法
     *
     * @param username
     * @param password
     * @return
     */

    @PostMapping("/register")
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password){
        if (username.isEmpty()||password.isEmpty()) throw new UserException("请补全注册信息");
        boolean flag=userService.register(username,password);
        if(flag) {
            log.info("用户注册成功");
            return Result.OK();
        }
        log.error("注册失败");
        return Result.Fail();
    }

    /**
     * 登录方法
     *
     * @param username
     * @param password
     * @return
     */
    @HystrixCommand(
        // 设置回退方法为loginFallbackMethod
        fallbackMethod = "loginFallbackMethod",
        commandProperties = {
            // 设置线程隔离超时时间为1000毫秒
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            // 启用执行超时
            @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
        },
        // 设置线程池key为helloPool
        threadPoolKey = "helloPool",
        threadPoolProperties = {
                // 设置核心线程数为2
                @HystrixProperty(name = "coreSize", value = "2")
        })
    @PostMapping("/login")
    public ResultWithMap<String> login(@RequestParam("username") String username,
                                 @RequestParam("password") String password) throws InterruptedException {
        // 模拟耗时操作
        //sleep(1000);
        if (username.isEmpty()||password.isEmpty()) throw new UserException("请补全登录信息");
        Map<String, String> map = userService.login(username, password);
        log.info("用户登录成功");
        return ResultWithMap.OK(map);
    }

    public ResultWithMap<String> loginFallbackMethod(String username, String password){
        log.info("用户登录失败");
        return ResultWithMap.Fail();
    }
    /**
     * 查询用户操作
     *
     * @param username
     * @return
     */
    @HystrixCommand(
        // 设置熔断方法
        fallbackMethod = "getUserFallbackMethod",
        // 设置命令属性
        commandProperties = {
            // 设置执行隔离线程超时时间为5000毫秒
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",
                    value = "5000"),
            // 设置执行隔离策略为信号量
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY,
                    value = "SEMAPHORE"),
            // 设置信号量最大并发请求数为2
            @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS,
                    value = "2")
        })
    @GetMapping("/info")
    public Result<User> getUser(@RequestParam("username") String username){
        if (username.isEmpty())throw new UserException("用户ID信息不可为空");
        User user = (User) userService.loadUserByUsername(username);
        boolean flag= user != null;
        if (flag) return Result.OK(user);
        return Result.Fail("用户不存在");
    }


    public Result<User> getUserFallbackMethod(String username){
        log.info("用户不存在");
        return Result.Fail();
    }




    /**
     * 先对文件头像进行检查是否是图片，在上传到本地仓库
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PutMapping("/uploads")
    public Result update(@RequestBody MultipartFile file) throws IOException {
        String url=fileUtils.checkPhotoAndUploads(file);
        boolean flag= url!=null;
        if(flag){
            String userId = UserContext.get();
            User user = userService.getById(userId);
            user.setAvatarUrl(url);
            userService.update(user);
            log.info("用户头像上传成功");
            return Result.OK(url);
        }
        return Result.Fail("文件上传失败");
    }

}
