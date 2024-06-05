package com.zhang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhang.client.CommentClient;
import com.zhang.client.VideoClient;
import com.zhang.entity.Comment;
import com.zhang.entity.UserLike;
import com.zhang.entity.Video;
import com.zhang.exception.UserException;
import com.zhang.mapper.UserLikeMapper;
import com.zhang.service.UserLikeService;
import com.zhang.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserLikeServiceImpl implements UserLikeService {

    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private VideoClient videoClient;
    @Autowired
    private CommentClient commentClient;

    /**
     * 用户点赞后的处理
     *
     * @param userId
     * @param videoId
     * @param commentId
     * @return
     */
    @Override
    public boolean add(String userId, String videoId, String commentId) {
        QueryWrapper<UserLike> qw=new QueryWrapper<>();
        if (DataUtils.judge(videoId)){
            //判断是否点赞
            qw.eq("user_id",userId).eq("video_id",videoId);
            if(userLikeMapper.selectCount(qw)!=0){
                throw new UserException("已点赞,请不要重复操作");
            }
            //更新video表
            Video video = videoClient.selectById(videoId).getData();
            video.setUpdatedAt(new Date());
            video.setLikeCount(video.getLikeCount()+1);
            videoClient.updateById(video);
            //更新user_like表中的数据
            UserLike userLike = new UserLike();
            userLike.setUserId(userId);
            userLike.setVideoId(videoId);
            userLike.setCreatedAt(new Date());
            return userLikeMapper.insert(userLike)!=0;
        }
        if (DataUtils.judge(commentId)){
            //判断是否点赞
            qw.eq("user_id",userId).eq("comment_id",commentId);
            if(userLikeMapper.selectCount(qw)!=0){
                throw new UserException("已点赞,请不要重复操作");
            }
            //更新comment表
            Comment comment = new Comment();
            comment.setUpdatedAt(new Date());
            comment.setLikeCount(comment.getLikeCount()+1);
            commentClient.updateById(comment);
            //更新user_like表中的数据
            UserLike userLike = new UserLike();
            userLike.setUserId(userId);
            userLike.setCommentId(commentId);
            userLike.setCreatedAt(new Date());
            return userLikeMapper.insert(userLike)!=0;
        }
        return false;
    }

    /**
     * 用户取消点赞后的处理
     *
     * @param userId
     * @param videoId
     * @param commentId
     * @return
     */
    @Override
    public boolean delete(String userId, String videoId, String commentId) {
        QueryWrapper<UserLike> qw=new QueryWrapper<>();
        if (DataUtils.judge(videoId)){
            //判断是否点赞
            qw.eq("user_id",userId).eq("video_id",videoId);
            if(userLikeMapper.selectCount(qw)==0){
                throw new UserException("未点赞,请先点赞");
            }
            //更新video表
            Video video = videoClient.selectById(videoId).getData();
            video.setUpdatedAt(new Date());
            video.setLikeCount(video.getLikeCount()-1);
            videoClient.updateById(video);
            //存储到user_like表
            QueryWrapper<UserLike> qw1=new QueryWrapper<>();
            qw1.eq("user_id", userId).eq("video_id", videoId);
            List<UserLike> userLikes = userLikeMapper.selectList(qw1);
            for (UserLike userLike : userLikes) {
                userLike.setDeletedAt(new Date());
            }
            return userLikeMapper.delete(qw1)!=0;
        }
        if (DataUtils.judge(commentId)){
            //判断是否点赞
            qw.eq("user_id",userId).eq("comment_id",commentId);
            if(userLikeMapper.selectCount(qw)==0){
                throw new UserException("未点赞,请先点赞");
            }
            //更新comment表
            Comment comment = new Comment();
            comment.setUpdatedAt(new Date());
            comment.setLikeCount(comment.getLikeCount()-1);
            commentClient.updateById(comment);
            //存储到user_like表
            QueryWrapper<UserLike> qw1=new QueryWrapper<>();
            qw1.eq("user_id", userId).eq("comment_id", commentId);
            List<UserLike> userLikes = userLikeMapper.selectList(qw1);
            for (UserLike userLike : userLikes) {
                userLike.setDeletedAt(new Date());
            }
            return  userLikeMapper.delete(qw1)!=0;
        }
        return false;
    }

    /**
     * 查询指定用户的点赞（喜欢）列表
     * @param userId
     * @param page_size
     * @param page_num
     * @return
     */
    @Override
    public List<Video> list(String userId,Integer page_size,Integer page_num) {
        QueryWrapper<UserLike> qw=new QueryWrapper<>();
        qw.eq("user_id",userId);
        List<UserLike> userLikes = userLikeMapper.selectList(qw);
        ArrayList<Video> videos1 = new ArrayList<>();
        for (UserLike userLike : userLikes) {
            String videoId = userLike.getVideoId();
            Video video = videoClient.selectById(videoId).getData();
            videos1.add(video);
        }
        //实现对List的分页查询
        int startIndex = (page_num - 1) * page_size;
        int endIndex = Math.min(startIndex + page_size, videos1.size());
        return videos1.subList(startIndex, endIndex);
    }
}
