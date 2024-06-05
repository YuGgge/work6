package com.zhang.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang
 * @date  2024/2/7
 * @Description 实现视频存储列表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video implements Serializable{
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("user_id")
    private long userId;
    @TableField("video_url")
    private String videoUrl;
    @TableField( "cover_url")
    private String coverUrl;
    private String title;
    private String description;
    @TableField( "visit_count")
    private int visitCount;
    @TableField( "like_count")
    private int likeCount;
    @TableField( "comment_count")
    private int commentCount;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("created_at")
    private Date createdAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField( "updated_at")
    private Date updatedAt;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("deleted_at")
    private Date deletedAt;
    @JsonIgnore
    private int deleted;
}
