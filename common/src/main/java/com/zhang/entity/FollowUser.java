package com.zhang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhang
 * &#064;date   2024/2/7
 * &#064;Description  用于实现关注，好友查询功能实现
 */
@TableName("user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowUser implements Serializable {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String username;
    @TableField("avatar_url")
    String avatarUrl;
}
