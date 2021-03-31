package com.scluis.service;

import com.scluis.po.Comment;

import java.util.List;

/**
 * Created by Sichengluis on 2021/2/2 17:56
 */
public interface commentService {
    /**
     * 功能描述: 获取某一篇博客的所有评论
     * @Param: [id 博客id]
     * @Return: java.util.List<com.scluis.po.Comment>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:19
     */
    List<Comment> getCommentList(Long id);

    Comment saveComment(Comment comment);

}
