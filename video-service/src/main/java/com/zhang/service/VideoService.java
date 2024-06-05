package com.zhang.service;

import com.zhang.entity.Video;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface VideoService {
    List<Video> feed(String timestamp);

    boolean publish(Long user_id,String video_url,String title,String description);

    List<Video> list(Long user_id,
                     Integer page_num,
                     Integer page_size);

    Integer getTotal(long userId);

    List<Video> search(String keywords,
                       Integer page_num,
                       Integer page_size,
                       String from_date,
                       String to_date,
                       String username);
    List<Video> popular(Integer page_size,Integer page_num);

    void addVisitCount(String pathInfo);
    Video selectById(String id);

    boolean updateById(Video video);
}
