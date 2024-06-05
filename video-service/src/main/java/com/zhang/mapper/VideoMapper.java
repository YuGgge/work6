package com.zhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface VideoMapper extends BaseMapper<Video>{

/*    @Insert("INSERT INTO video(user_id, video_url, title, description, created_at) " +
            "VALUES(#{userId}, #{videoUrl}, #{title}, #{description}, #{createdAt})")
    int insertVideo(Video video);*/

    @Select("SELECT * FROM video")
    List<Video> selectList();

    @Select("SELECT * FROM video WHERE created_at > #{time}")
    List<Video> selectVideosAfterTime(@Param("time") String time);

    @Select("SELECT * FROM video WHERE user_id = #{userId} ORDER BY created_at LIMIT #{pageSize} OFFSET #{offset}")
    List<Video> selectPageByUser(@Param("userId") Long userId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    @Select("SELECT COUNT(*) FROM video WHERE user_id = #{userId}")
    int selectCountByUser(@Param("userId") Long userId);
    /*@Results(id="VideoMapper",value = {
            @Result(column="created_at",property="created_at",jdbcType= JdbcType.TIMESTAMP),
            @Result(column="updated_at",property="updated_at",jdbcType= JdbcType.TIMESTAMP),
            @Result(column="deleted_at",property="deleted_at",jdbcType= JdbcType.TIMESTAMP),
            @Result(column="video_url",property="video_url",jdbcType= JdbcType.VARCHAR),
            @Result(column="cover_url",property="cover_url",jdbcType= JdbcType.VARCHAR),
            @Result(column="user_id",property="user_id",jdbcType= JdbcType.BIGINT),
            @Result(column="visit_count",property="visit_count",jdbcType= JdbcType.INTEGER),
            @Result(column="like_count",property="like_count",jdbcType= JdbcType.INTEGER),
            @Result(column="comment_count",property="comment_count",jdbcType= JdbcType.INTEGER)
    })
    @Select("select * from video ;")
    List<Video> getAll();

    @ResultMap(value="VideoMapper")
    @Select("SELECT * FROM video WHERE created_at > FROM_UNIXTIME(#{timestamp} / 1000)")
    List<Video> getVideosAfterTimestamp(@Param("timestamp") String timestamp);


    @ResultMap(value="VideoMapper")
    @Select("SELECT * FROM video WHERE (user_id = #{user_id}) LIMIT #{page_num},#{page_size}")
    List<Video> getVideoByPage(long user_id, Integer page_num, Integer page_size);*/
}
