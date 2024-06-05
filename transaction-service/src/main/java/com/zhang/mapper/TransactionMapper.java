package com.zhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.entity.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * @author zhang
 * @date 2024/4/23
 * @Description
 */

@Mapper
public interface TransactionMapper extends BaseMapper<Transaction> {


    @Insert("INSERT INTO transaction (id, user_id, content,created_at) VALUES (#{id}, #{userId}, #{content}, #{createdAt})")
    int insert(Transaction transaction);

    @Update("UPDATE transaction SET is_completed = #{isCompleted} WHERE id = #{id} AND deleted = 0 ")
    int updateById(Transaction transaction);



    @Select("SELECT id, user_id, content, created_at, updated_at, is_completed FROM transaction " +
            "WHERE user_id = #{userId} AND deleted = 0 ")
    List<Transaction> selectListByUser(Long userId);

    @Select("SELECT id, user_id, content, created_at, updated_at, is_completed " +
            "FROM transaction WHERE user_id = #{userId} AND deleted = 0 " +
            "ORDER BY created_at LIMIT #{pageSize} OFFSET #{offset}")
    List<Transaction> selectAllList(@Param("userId") Long userId, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    @Select("SELECT id, user_id, content, created_at, updated_at, is_completed " +
            "FROM transaction WHERE user_id = #{userId} AND is_completed = #{isCompleted} AND deleted = 0 ORDER BY created_at " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Transaction> selectListByCompleted(@Param("userId") Long userId, @Param("isCompleted") boolean isCompleted, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);

    @Select("SELECT id, user_id, content, created_at, updated_at, is_completed " +
            "FROM transaction where user_id = #{userId} and content LIKE CONCAT('%', #{keyword}, '%') AND deleted = 0 ORDER BY created_at " +
            "LIMIT #{pageSize} OFFSET #{offset}")
    List<Transaction> searchByKey(@Param("userId") Long userId,@Param("keyword") String keyword, @Param("pageSize") Integer pageSize, @Param("offset") Integer offset);



    // 删除实现逻辑删除
    @Update("UPDATE transaction SET delete = 1 WHERE user_id = #{userId} AND is_completed = #{isCompleted}")
    boolean removeCompleted(@Param("userId") Long userId, @Param("isCompleted") boolean isCompleted);

    @Update("UPDATE transaction SET delete = 1 WHERE user_id = #{userId}")
    boolean removeAll(@Param("userId") Long userId);

    @Update("UPDATE transaction SET delete = 1 WHERE id = #{id}")
    boolean remove(@Param("id") Long id);

}
