package com.zhang.controller;

import com.zhang.entity.Comment;
import com.zhang.exception.UserException;
import com.zhang.service.CommentService;
import com.zhang.utils.DataUtils;
import com.zhang.utils.UserContext;
import com.zhang.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author zhang
 * &#064;date  2024/2/10
 * &#064;Description  评论实现Controller层
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 评论发布
     * @param video_id
     * @param comment_id
     * @param content
     * @return
     */
    @PostMapping("/publish")
    public Result publish(@RequestParam(value = "video_id",required = false)String video_id,
                          @RequestParam(value = "comment_id",required = false)String comment_id,
                          @RequestParam(value = "content",required = false) String content){
        if (content.isEmpty()||(video_id.isEmpty()&&comment_id.isEmpty())){
            throw new UserException("操作错误,点赞信息不可为空");
        }
        String userId = UserContext.get();
        //数据校验设置值
        String videoId = DataUtils.validation(video_id);
        String commentId = DataUtils.validation(comment_id);
        //判断执行
        boolean flag = commentService.publish(Long.valueOf(userId),videoId, commentId, content);
        if(flag) {
            log.info("点赞成功");
            return Result.OK();
        }
        log.error("点赞失败");
        return Result.Fail();
    }


    @GetMapping("/list")
    public Result list(@RequestParam(value = "video_id",required = false)String video_id,
                       @RequestParam(value = "comment_id",required = false)String comment_id,
                       @RequestParam("page_size") Integer page_size,
                       @RequestParam("page_num") Integer page_num){
        if ((video_id.isEmpty()&&comment_id.isEmpty())){
            throw new UserException("操作错误,信息不可为空");
        }
        //数据校验设置值
        String videoId = DataUtils.validation(video_id);
        String commentId = DataUtils.validation(comment_id);
        List<Comment> list = commentService.list(videoId, commentId, page_size, page_num);
        if(list!=null) {
            log.info("查询评论成功");
            return Result.OK(list);
        }
        log.error("查询评论失败");
        return Result.Fail();
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam(value = "video_id",required = false)String video_id,
                         @RequestParam(value = "comment_id",required = false)String comment_id,
                         @RequestParam("id")String Id){
        //数据校验设置值
        String videoId = DataUtils.validation(video_id);
        String commentId = DataUtils.validation(comment_id);
        String id = DataUtils.validation(Id);
        boolean flag = commentService.delete(videoId, commentId,id);
        if(flag) {
            log.info("删除评论成功");
            return Result.OK();
        }
        log.error("删除评论失败");
        return Result.Fail();
    }

    @PostMapping("/updateById")
    Result updateById(@RequestParam("comment") Comment comment){
        boolean flag = commentService.updateById(comment);
        return flag ? Result.OK() : Result.Fail();
    }
}
