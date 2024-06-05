package com.zhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.client.VideoClient;
import com.zhang.entity.Comment;
import com.zhang.entity.Video;
import com.zhang.exception.UserException;
import com.zhang.mapper.CommentMapper;
import com.zhang.service.CommentService;
import com.zhang.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private VideoClient videoClient;


    /**
     * 对视频或评论进行评论
     * @param userId
     * @param videoId
     * @param commentId
     * @param content
     * @return
     */
    @Override
    public boolean publish(Long userId,String videoId, String commentId, String content) {
        Comment comment = new Comment();
        //原始评论
        if (DataUtils.judge(videoId)) {
            //设置视频评论数量
            Video video = (Video) videoClient.selectById(videoId).getData();
            video.setCommentCount(video.getCommentCount()+1);
            video.setUpdatedAt(new Date());
            videoClient.updateById(video);
            comment.setVideoId(videoId);
        }
        //子评论
        if (DataUtils.judge(commentId)) {
            comment.setParentId(commentId);
            Comment commentFather = commentMapper.selectById(commentId);
            if (commentFather == null)throw new UserException("该评论不存在无法评论");
            comment.setUserId(commentFather.getUserId());
            //设置父评论信息
            commentFather.setChildCount(commentFather.getChildCount()+1);
            commentFather.setUpdatedAt(new Date());
            commentMapper.updateById(commentFather);
        }
        comment.setUserId(userId);
        comment.setContent(content);
        comment.setCreatedAt(new Date());
        return commentMapper.insert(comment)!=0;
    }

    /**
     * 根据videoId或commentId获得评论列表
     * @param videoId
     * @param commentId
     * @param page_size
     * @param page_num
     * @return
     */
    @Override
    public List<Comment> list(String videoId, String commentId, Integer page_size, Integer page_num) {
        QueryWrapper<Comment> qw = getComment(videoId, commentId);
        IPage<Comment> page=new Page<>(page_num,page_size);
        IPage<Comment> iPage = commentMapper.selectPage(page, qw);
        return iPage.getRecords();
    }

    /**
     * 可以根据Id,videoId,commentId(即父评论)进行删除
     * @param videoId
     * @param commentId
     * @param id
     * @return
     */
    @Override
    public boolean delete(String videoId, String commentId,String id) {
        QueryWrapper<Comment> qw = getComment(videoId, commentId);
        if (DataUtils.judge(id)){
            qw.eq("id",id);
        }
        List<Comment> comments = commentMapper.selectList(qw);
        for (Comment comment : comments) {
            comment.setDeletedAt(new Date());
            commentMapper.updateById(comment);
        }
        return commentMapper.delete(qw)!=0;
    }

    @Override
    public boolean updateById(Comment comment) {
        return commentMapper.updateById(comment) != 0;
    }

    /**
     * 根据videoId或commentId查询评论
     *
     * @param videoId
     * @param commentId
     * @return
     */
    public QueryWrapper<Comment> getComment(String videoId, String commentId){
        QueryWrapper<Comment> qw=new QueryWrapper<>();
        if (DataUtils.judge(videoId)) {
            qw.eq("video_id", videoId);
        }
        if (DataUtils.judge(commentId)) {
            qw.eq("parent_id", commentId);
        }
        return qw;
    }
}
