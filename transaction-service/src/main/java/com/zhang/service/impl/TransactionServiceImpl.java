package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.entity.Transaction;
import com.zhang.mapper.TransactionMapper;
import com.zhang.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhang
 * @date 2024/4/23
 * @Description
 */

@Service
public class TransactionServiceImpl implements TransactionService  {

    @Autowired
    private TransactionMapper transactionMapper;

    /**
     * 插入一条待办
     * @param userId
     * @param content
     * @return
     */
    @Override
    public boolean save(Long userId, String content) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setContent(content);
        transaction.setCreatedAt(new Date());
        return transactionMapper.insert(transaction) != 0;
    }

    /**
     * 更改用户一条事务
     * @param id
     * @param completed
     * @return
     */
    @Override
    public boolean mark(Long id, boolean completed) {
        Transaction transaction = transactionMapper.selectById(id);
        transaction.setCompleted(completed);
        transaction.setUpdatedAt(new Date());
        return transactionMapper.updateById(transaction) != 0;
    }

    /**
     * 更改用户所有事务
     * @param userId
     * @param completed
     * @return
     */
    @Override
    public boolean markAll(Long userId, boolean completed) {
        List<Transaction> transactions = transactionMapper.selectListByUser(userId);
        for (Transaction transaction : transactions) {
            transaction.setCompleted(completed);
            transaction.setUpdatedAt(new Date());
            transactionMapper.updateById(transaction);
        }
        return true;
    }

    /**
     * 查看所有 未完成/已完成
     * @param userId
     * @param isCompleted
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Transaction> getListByStatus(Long userId, boolean isCompleted, Integer pageNum, Integer pageSize) {
        return transactionMapper.selectListByCompleted(userId, isCompleted, pageSize,(pageNum - 1) * pageSize);
    }

    /**
     * 查看所有事项
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Transaction> getAll(Long userId, Integer pageNum, Integer pageSize) {
        return transactionMapper.selectAllList(userId, pageSize,(pageNum - 1) * pageSize);
    }

    /**
     * 根据关键词查询
     * @param userId
     * @param keyword
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<Transaction> searchByKey(Long userId, String keyword, Integer pageNum, Integer pageSize) {
        return transactionMapper.searchByKey(userId, keyword, pageSize, (pageNum - 1) * pageSize);
    }

    /**
     * 实现逻辑删除
     * @param userId
     * @param isCompleted
     * @return
     */
    @Override
    public boolean removeAll(Long userId, boolean isCompleted) {
        return transactionMapper.removeCompleted(userId, isCompleted);
    }

    @Override
    public boolean removeAll(Long userId) {
        return transactionMapper.removeAll(userId);
    }

    @Override
    public boolean remove(Long id) {
        return transactionMapper.remove(id);
    }


}
