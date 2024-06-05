package com.zhang.service;

import com.zhang.entity.Comment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CommentService {
    boolean publish(Long userId,String videoId,String commentId,String content);

    List<Comment> list(String videoId,String commentId,
                       Integer page_size,Integer page_num);

    boolean delete(String videoId,String commentId,String id);

    boolean updateById(Comment comment);
}
