package com.zhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.entity.Transaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhang
 * @date 2024/4/23
 * @Description
 */

@Transactional
public interface TransactionService {

    boolean save(Long userId, String content);

    boolean mark(Long id, boolean completed);

    boolean markAll(Long userId, boolean completed);

    List<Transaction> getListByStatus(Long UserId, boolean isCompleted, Integer pageNum, Integer pageSize);

    List<Transaction> getAll(Long UserId, Integer pageNum, Integer pageSize);

    List<Transaction> searchByKey(Long userId, String keyword, Integer pageNum, Integer pageSize);

    boolean removeAll(Long userId, boolean isCompleted);

    boolean removeAll(Long userId);

    boolean remove(Long id);
}
