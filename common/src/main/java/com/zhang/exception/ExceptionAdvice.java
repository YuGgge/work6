package com.zhang.exception;

import com.zhang.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {



    /**
     * 捕获用户操作异常
     * @param ex
     * @return
     */
    @ExceptionHandler(UserException.class)
    @ResponseBody
    public Result processUserException(UserException ex){
        log.error("业务异常"+ ex);
        return Result.Fail("操作错误:" + ex.getMessage());
    }

    /**
     * 捕获用户注册时的用户名已存在异常
     * @param ex
     * @return
     */
    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public Result processSQLException(java.sql.SQLIntegrityConstraintViolationException ex){
        String Message=ex.getMessage();
        if (Message.contains("Duplicate entry")){
            log.error("操作异常"+ ex);
            return Result.Fail("操作错误:用户名已存在，请重新输入");
        }
        log.error("未知SQL异常:"+ex);
        return Result.Fail("未知SQL异常");
    }


    /**
     * 捕获全部异常
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result processException(Exception ex){
        log.error("后端异常"+ ex);
        return Result.Fail(ex.getMessage());
    }
}
