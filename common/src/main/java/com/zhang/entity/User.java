package com.zhang.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * @author zhang
 * @date  2024/2/7
 * @Description 用户
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    /*@TableId(type = IdType.ASSIGN_ID)*/
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    private String username;
    @JsonIgnore
    private String password;
    @TableField("avatar_url")
    String avatarUrl;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField( "created_at")
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
