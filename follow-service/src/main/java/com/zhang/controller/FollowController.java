package com.zhang.controller;

import com.zhang.entity.Follow;
import com.zhang.entity.FollowUser;
import com.zhang.entity.User;
import com.zhang.exception.UserException;
import com.zhang.service.FollowService;
import com.zhang.utils.UserContext;
import com.zhang.vo.Result;
import com.zhang.vo.ResultWithList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhang
 * &#064;date  2024/2/10
 * &#064;Description  实现关注Controller层
 */
@RestController
@RequestMapping
@Slf4j
public class FollowController {

    @Autowired
    private FollowService followService;

    /**
     * 实现关注和取消关注
     * @param followId
     * @param action
     * @return
     */
    @PostMapping("/relation/action")
    public Result action(@RequestParam("to_user_id")String followId,
                         @RequestParam("action_type") Integer action){
        if (followId.isEmpty()){
            throw new UserException("操作错误,用户Id信息不可为空");
        }
        String userId = UserContext.get();

        boolean flag=( action== 0) ? followService.add(userId, followId)
                :followService.delete(userId, followId);
        if (flag){
            log.info("关注用户成功");
            return Result.OK();
        }
        log.error("关注用户失败");
        return Result.Fail("关注用户失败,请输入正确指示");
    }


    /**
     * 查看指定用户的粉丝列表
     * @param userId
     * @param page_size
     * @param page_num
     * @return
     */
    @GetMapping("/following/list")
    public ResultWithList<FollowUser> list(@RequestParam("user_id") String userId,
                                       @RequestParam("page_size") Integer page_size,
                                       @RequestParam("page_num") Integer page_num){
        if (userId.isEmpty()){
            throw new UserException("操作错误,Id信息不可为空");
        }
        List<FollowUser> list = followService.list(userId, page_size, page_num);
        Long total = Long.valueOf(followService.getTotal(userId));
        boolean flag = list!=null;
        if (flag) {
            log.info("查询成功");
            return ResultWithList.OK(list,total);
        }
        log.error("查询失败");
        return ResultWithList.Fail();
    }

    /**
     * 查看指定用户的关注列表
     * @param followId
     * @param page_size
     * @param page_num
     * @return
     */
    @GetMapping("/follower/list")
    public ResultWithList<FollowUser> listFollow(@RequestParam("user_id") String followId,
                             @RequestParam("page_size") Integer page_size,
                             @RequestParam("page_num") Integer page_num){
        if (followId.isEmpty()){
            throw new UserException("操作错误,Id信息不可为空");
        }
        List<FollowUser> list = followService.listFollow(followId, page_size, page_num);
        Long total = Long.valueOf(followService.getTotalFollow(followId));
        boolean flag = list!=null;
        if (flag) {
            log.info("查询粉丝列表");
            return ResultWithList.OK(list,total);
        }
        log.error("查询粉丝失败");
        return ResultWithList.Fail();
    }

    /**
     * 查看当前登录用户的好友信息
     * @param page_size
     * @param page_num
     * @return
     */
    @GetMapping("/friends/list")
    public Result listFriend(@RequestParam("page_size") Integer page_size,
                             @RequestParam("page_num") Integer page_num){
        String userId = UserContext.get();
        List<FollowUser> listFriend = followService.listFriend(userId, page_size, page_num);
        boolean flag = listFriend!=null;
        if (flag) {
            log.info("查询好友成功");
            return Result.OK(listFriend);
        }
        log.error("查询失败");
        return Result.Fail();
    }

}
