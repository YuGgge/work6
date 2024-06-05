package com.zhang.service;

import com.zhang.entity.Video;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserLikeService {
    boolean add(String userId,String videoId,String commentId);
    boolean delete(String userId,String videoId,String commentId);

    List<Video> list(String userId,Integer page_size,Integer page_num);
}
