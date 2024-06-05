package com.zhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.entity.Transaction;
import com.zhang.entity.User;
import com.zhang.vo.Result;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface UserService{

    boolean register(String username,String password);
    Map<String, String> login(String username, String password);
    boolean update(User user);
    UserDetails loadUserByUsername(String username);

    User getById(String userId);

}
