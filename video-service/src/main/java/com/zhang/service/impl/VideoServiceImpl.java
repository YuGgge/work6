package com.zhang.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.client.UserClient;
import com.zhang.entity.User;
import com.zhang.entity.Video;
import com.zhang.mapper.VideoMapper;
import com.zhang.service.VideoService;
import com.zhang.utils.DataUtils;
import com.zhang.utils.RedisUtil;
import com.zhang.utils.TimeChangeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${image.urlPath}")
    private String urlPath;
    /**
     * 获取首页视频流
     *
     * @param timestamp
     * @return
     */
    @Override
    public List<Video> feed(String timestamp) {
        if (!Objects.equals(timestamp, "")) {
            String time = TimeChangeUtils.timeStampDate(timestamp);
            return videoMapper.selectVideosAfterTime(time);
        } else {
            return videoMapper.selectList();
        }
    }

    /**
     * 投稿
     *
     * @param userId
     * @param video_url
     * @param title
     * @param description
     * @return
     */
    @Override
    public boolean publish(Long userId, String video_url, String title, String description) {
        //生成新的视频
        Video video = new Video();
        video.setUserId(userId);
        video.setVideoUrl(video_url);
        video.setTitle(title);
        video.setDescription(description);
        video.setCreatedAt(new Date());
        return videoMapper.insert(video) != 0;
    }

    /**
     * 根据 user_id 查看指定人的发布列表
     *
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Video> list(Long userId, Integer pageNum, Integer pageSize) {
        return videoMapper.selectPageByUser(userId, pageSize,(pageNum - 1) * pageSize);
    }

    /**
     * 根据用户ID获得用户发布视频列表
     *
     * @param userId
     * @return
     */
    @Override
    public Integer getTotal(long userId) {
        return videoMapper.selectCountByUser(userId);
    }

    /**
     * 搜索指定关键字的视频，将会从以下字段进行搜索
     * 标题（title）
     * 描述（description）
     *
     * @param keywords
     * @param page_num
     * @param page_size
     * @param from_date
     * @param to_date
     * @param username
     * @return
     */
    @Override
    public List<Video> search(String keywords, Integer page_num,
                              Integer page_size, String from_date,
                              String to_date, String username) {
        QueryWrapper<Video> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("title",keywords).or().
                like("description",keywords);
        if (DataUtils.judge(from_date)) {
            String fromTime = TimeChangeUtils.timeStampDate(from_date);
            queryWrapper.gt("created_at",fromTime);
        }
        if (DataUtils.judge(to_date)) {
            String toTime = TimeChangeUtils.timeStampDate(to_date);
            queryWrapper.lt("created_at",toTime);
        }
        if (DataUtils.judge(username)){
            User user = userClient.loadUserByUsername(username).getData();
            queryWrapper.eq("user_id",user.getId());
        }
        Page<Video> page = new Page<>(page_num, page_size);
        IPage<Video> iPage = videoMapper.selectPage(page, queryWrapper);
        return iPage.getRecords();
    }

    /**
     * 存储到redis的排序方法
     * @param page_size
     * @param page_num
     * @return
     */
    @Override
    public List<Video> popular(Integer page_size, Integer page_num) {
        //存储所有视频id到redis，已经存在的更新覆盖
        String key = "热门排行榜-video";
        List<Video> videos = videoMapper.selectList(null);
        for (Video video : videos) {
            redisUtil.addScore(key,String.valueOf(video.getId()),video.getVisitCount());
        }
        //提取信息
        Set<Object> videoIds = redisUtil.getAllMembers(key);
        ArrayList<Video> videos1 = new ArrayList<>();
        for (Object videoId : videoIds) {
            Video video = videoMapper.selectById((Serializable) videoId);
            videos1.add(video);
        }
        //实现对List的分页查询
        int startIndex = (page_num - 1) * page_size;
        int endIndex = Math.min(startIndex + page_size, videos1.size());
        return videos1.subList(startIndex, endIndex);
    }

    @Override
    public void addVisitCount(String pathInfo) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        String videoUrl = urlPath + pathInfo;
        queryWrapper.eq("video_url", videoUrl);
        List<Video> videos = videoMapper.selectList(queryWrapper);
        for (Video video : videos) {
            video.setVisitCount(video.getVisitCount()+1);
            video.setUpdatedAt(new Date());
            videoMapper.updateById(video);
        }
    }

    @Override
    public Video selectById(String id) {
        return videoMapper.selectById(id);
    }

    @Override
    public boolean updateById(Video video) {
        return videoMapper.updateById(video) != 0;
    }


}