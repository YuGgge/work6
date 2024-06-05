package com.zhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.entity.Follow;
import com.zhang.entity.FollowUser;
import com.zhang.exception.UserException;
import com.zhang.mapper.FollowMapper;
import com.zhang.mapper.FollowUserMapper;
import com.zhang.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowMapper followMapper;
    @Autowired
    private FollowUserMapper followUserMapper;

    /**
     * 添加关注操作
     * @param userId
     * @param followId
     * @return
     */
    @Override
    public boolean add(String userId, String followId) {
        if(userId.equals(followId)){
            throw new UserException("用户不可以关注自己");
        }
        //判断是否关注
        QueryWrapper<Follow> qw=new QueryWrapper<>();
        qw.eq("user_id",userId).eq("follow_user_id",followId);
        if(followMapper.selectCount(qw)!=0){
            throw new UserException("已关注,请不要重复关注操作");
        }
        Follow follow = new Follow();
        follow.setUserId(userId);
        follow.setFollowId(followId);
        follow.setCreatedAt(new Date());
        return followMapper.insert(follow)!=0;
    }

    /**
     * 取消关注操作
     * @param userId
     * @param followId
     * @return boolean
     */
    @Override
    public boolean delete(String userId, String followId) {
        if(userId.equals(followId)){
            throw new UserException("输入错误，用户不可关注自己");
        }
        QueryWrapper<Follow> qw=new QueryWrapper<>();
        qw.eq("user_id",userId).eq("follow_user_id",followId);
        if(followMapper.selectCount(qw)==0){
            throw new UserException("未关注,请先关注");
        }
        Follow follow = followMapper.selectOne(qw);
        return followMapper.deleteById(follow)!=0;
    }

    /**
     * 关注列表
     * @param userId
     * @param page_size
     * @param page_num
     * @return List<User>
     */
    @Override
    public List<FollowUser> list(String userId, Integer page_size, Integer page_num) {
        QueryWrapper<Follow> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        ArrayList<FollowUser> followUsers = new ArrayList<>();
        List<Follow> follows = followMapper.selectList(qw);
        for (Follow follow : follows) {
            FollowUser followUser = followUserMapper.selectById(follow.getFollowId());
            followUsers.add(followUser);
        }
        //实现对List的分页查询
        int startIndex = (page_num - 1) * page_size;
        int endIndex = Math.min(startIndex + page_size, followUsers.size());
        return followUsers.subList(startIndex, endIndex);
    }

    /**
     * 关注个数
     *
     * @param userId
     * @return
     */
    @Override
    public Integer getTotal(String userId) {
        QueryWrapper<Follow> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        return followMapper.selectCount(qw);
    }

    /**
     * 粉丝列表
     * @param followId
     * @param page_size
     * @param page_num
     * @return
     */
    @Override
    public List<FollowUser> listFollow(String followId, Integer page_size, Integer page_num) {
        QueryWrapper<Follow> qw=new QueryWrapper<>();
        qw.eq("follow_user_id",followId);
        List<Follow> follows = followMapper.selectList(qw);
        //这里搜索到的只是follow表中的关系，只能得到关联的用户id，给前端展示的应该是具体用户的信息，所以又封装了一个followUser。
        ArrayList<FollowUser> followUsers = new ArrayList<>();
        for (Follow follow : follows) {
            FollowUser followUser = followUserMapper.selectById(follow.getUserId());
            followUsers.add(followUser);
        }
        //实现对List的分页查询
        int startIndex = (page_num - 1) * page_size;
        int endIndex = Math.min(startIndex + page_size, followUsers.size());
        return followUsers.subList(startIndex, endIndex);
    }

    /**
     * 粉丝个数
     *
     * @param followId
     * @return
     */
    @Override
    public Integer getTotalFollow(String followId) {
        QueryWrapper<Follow> qw=new QueryWrapper<>();
        qw.eq("follow_user_id",followId);
        return followMapper.selectCount(qw);
    }

    /**
     * 朋友列表
     * @param userId
     * @param page_size
     * @param page_num
     * @return
     */
    @Override
    public List<FollowUser> listFriend(String userId, Integer page_size, Integer page_num) {
        QueryWrapper<Follow> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        ArrayList<FollowUser> listFriend = new ArrayList<>();
        List<Follow> follows = followMapper.selectList(qw);
        for (Follow follow : follows) {
            //判断关注的人是否关注自己
            String followId = follow.getFollowId();
            QueryWrapper<Follow> qw1=new QueryWrapper<>();
            qw.eq("user_id",followId);
            List<Follow> follows1 = followMapper.selectList(qw1);//follow关注的人列表
            for (Follow follow1 : follows1) {
                if(Objects.equals(follow1.getFollowId(), userId)){
                    FollowUser followUser = followUserMapper.selectById(follow1.getUserId());
                    listFriend.add(followUser);
                }
            }
        }
        //实现对List的分页查询
        int startIndex = (page_num - 1) * page_size;
        int endIndex = Math.min(startIndex + page_size, listFriend.size());
        return listFriend.subList(startIndex, endIndex);
    }
}
