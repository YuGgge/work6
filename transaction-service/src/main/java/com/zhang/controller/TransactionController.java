package com.zhang.controller;

import com.zhang.entity.Transaction;
import com.zhang.service.TransactionService;
import com.zhang.utils.UserContext;
import com.zhang.vo.Result;
import com.zhang.vo.ResultWithList;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhang
 * @date 2024/4/23
 * @Description
 */

@RestController
@RequestMapping("/transaction")
@Slf4j
public class TransactionController {


    @Autowired
    private TransactionService transactionService;


    /**
     * 插入一条待办
     * @param content
     * @return
     */
    @PostMapping("/save")
    private Result save(@RequestParam("content") String content){
        String userId = UserContext.get();
        return transactionService.save(Long.valueOf(userId), content) ? Result.OK() : Result.Fail("插入失败");
    }

    /**
     * 更改一条待办/未办
     * @param id
     * @param isCompleted
     * @return
     */
    @PutMapping("/markOne")
    private Result mark(@RequestParam("transaction_id") Long id,
                        @RequestParam("is_completed") boolean isCompleted){
        return transactionService.mark(id, isCompleted) ? Result.OK() : Result.Fail("插入失败");
    }

    /**
     * 更改所有待办/未办
     * @param isCompleted
     * @return
     */
    @PutMapping("/markAll")
    private Result markAll(@RequestParam("is_completed") boolean isCompleted){
        String userId = UserContext.get();
        return transactionService.markAll(Long.valueOf(userId), isCompleted) ? Result.OK() : Result.Fail("插入失败");
    }

    /**
     * 查看所有 已完成/未完成/所有事项
     * @param isCompleted
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResultWithList<Transaction> getList(
            @RequestParam(value = "is_completed", required = false) Integer isCompleted,
            @RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize) {
        String userId = UserContext.get();
        if (isCompleted != null) {
            return ResultWithList.OK(transactionService.getListByStatus(Long.valueOf(userId), isCompleted !=0 , pageNum, pageSize));
        } else {
            return ResultWithList.OK(transactionService.getAll(Long.valueOf(userId), pageNum, pageSize));
        }
    }

    /**
     * 输入关键词查询事项
     *
     * @param keyword 查询关键词，不能为null或空字符串
     * @param pageNum 当前页码，默认值为1
     * @param pageSize 每页显示条数，默认值为10
     * @return 返回一个包含事项列表的ResultWithList对象
     *         如果查询成功，则状态码为200，返回包含事项列表的ResultWithList对象
     *         如果查询失败，则状态码不为200，返回相应的错误信息
     */
    @GetMapping("/search")
    public ResultWithList<Transaction> search(
            @RequestParam(value = "keyword") String keyword,
            @RequestParam(value = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") int pageSize) {

        String userId = UserContext.get();
        return ResultWithList.OK(transactionService.searchByKey(Long.valueOf(userId), keyword, pageNum, pageSize));
    }



    /**
     * 删除所有 已完成/未完成/所有事项
     * @param isCompleted
     * @return
     */
    @DeleteMapping("/removeAll")
    public Result removeAll(@RequestParam(value = "is_completed", required = false) Integer isCompleted) {
        String userId = UserContext.get();
        if (isCompleted != null) {
            return transactionService.removeAll(Long.valueOf(userId), isCompleted != 0) ? Result.OK() : Result.Fail("删除失败");
        } else {
            return transactionService.removeAll(Long.valueOf(userId)) ? Result.OK() : Result.Fail("删除失败");
        }
    }


    /**
     * 删除一条事项
     * @param id
     * @return
     */
    @DeleteMapping("/remove")
    public Result remove(@RequestParam("transaction_id") Long id) {
        return transactionService.remove(id) ? Result.OK() : Result.Fail("删除失败");
    }
}
