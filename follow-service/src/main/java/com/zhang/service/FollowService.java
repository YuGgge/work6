package com.zhang.service;

import com.zhang.entity.FollowUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FollowService {
    boolean add(String userId,String followId);
    boolean delete(String userId,String followId);
    List<FollowUser> list(String userId, Integer page_size, Integer page_num);
    Integer getTotal(String userId);
    List<FollowUser> listFollow(String followId,Integer page_size,Integer page_num);
    Integer getTotalFollow(String followId);
    List<FollowUser> listFriend(String userId,Integer page_size,Integer page_num);
}
