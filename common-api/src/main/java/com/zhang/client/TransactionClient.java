package com.zhang.client;

import com.zhang.entity.Transaction;
import com.zhang.vo.Result;
import com.zhang.vo.ResultWithList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhang
 * @date 2024/5/7
 * @Description
 */


//TODO   再header中添加accessToken
@FeignClient("transaction-service")
public interface TransactionClient {
    @GetMapping("/transaction/list")
    ResultWithList<Transaction> getList(@RequestParam(value = "page_num",required = false,defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "page_size",required = false,defaultValue = "1") Integer pageSize);
}
