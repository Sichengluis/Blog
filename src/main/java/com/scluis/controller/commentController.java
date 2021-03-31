package com.scluis.controller;

import com.scluis.po.Comment;
import com.scluis.po.User;
import com.scluis.service.blogService;
import com.scluis.service.commentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by Sichengluis on 2021/2/2 17:47
 */
@Controller
@RequestMapping("/comments")
public class commentController {
    @Autowired
    private commentService commentService;
    @Autowired
    private blogService blogService;
    //取出配置文件中定义的值并赋值给变量
    @Value("${comment.avatar}")
    private String avatar;
    /**
     * 功能描述: 返回某一个博客的所有评论
     * @Param: [blogId 博客id, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:52
     */
    @GetMapping("/{blogId}")
    public String getCommentList(@PathVariable Long blogId,Model model){
        model.addAttribute("comments",commentService.getCommentList(blogId));
        return "blog :: commentList";//对应前端的fragments
    }

    /**
     * 功能描述: 处理回复别人的评论和发布新评论请求
     * @Param: [comment]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:53
     */
    @PostMapping("/replyOrSubmit")
    public String replyOrSubmitComment(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user= (User) session.getAttribute("user");
        if(user!=null){
            //管理员
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
        }
        else {
            comment.setAvatar(avatar);
        }

        commentService.saveComment(comment);
        return "redirect:/comments/"+blogId;
    }

}
