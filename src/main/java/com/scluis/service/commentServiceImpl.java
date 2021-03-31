package com.scluis.service;

import com.scluis.po.Comment;
import com.scluis.repository.commentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sichengluis on 2021/2/3 16:39
 */
@Service
public class commentServiceImpl implements commentService{

    //暂时存放 迭代找出的 所有子回复的 集合
    private List<Comment> tempReplys = new ArrayList<>();
    @Autowired
    private commentRepository commentRepository;

    /**
     * 功能描述: 获得一篇博客的所有评论
     * @Param: [id 博客id]
     * @Return: java.util.List<com.scluis.po.Comment>
     * @Author: Sichengluis
     * @Date: 2021/2/5 15:29
     */
    @Override
    public List<Comment> getCommentList(Long id) {
        return getCommentsAfterAdjustment(commentRepository.findByBlogIdAndParentCommentIsNull(id,new Sort("createTime")));
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parrentCommentId=comment.getParentComment().getId();
        if(parrentCommentId!=-1){
            //该条评论是回复的一个父评论
            comment.setParentComment(commentRepository.findOne(parrentCommentId));
        }
        else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    /**
     * 功能描述: 将顶层评论放到一个新数组，并得到调整后（两级）的评论数组
     * @Param: [comments 顶层评论数组]
     * @Return: java.util.List<com.scluis.po.Comment>
     * @Author: Sichengluis
     * @Date: 2021/2/5 14:19
     */
    private List<Comment> getCommentsAfterAdjustment(List<Comment> comments) {
        //将comments复制到commentsTmp，防止更改comments导致数据库变化
        List<Comment> commentsTmp = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsTmp.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        adjustCommentsStructure(commentsTmp);
        return commentsTmp;
    }

    /**
     * 功能描述: 将评论的结构由多层嵌套调整成两层结构
     * @Param: [comments 顶层评论集合]
     * @Return: void
     * @Author: Sichengluis
     * @Date: 2021/2/5 14:14
     */
    private void adjustCommentsStructure(List<Comment> comments) {

        for (Comment comment : comments) {//comment每个顶层评论
            List<Comment> topReplys = comment.getReplyComments();//顶层评论的所有直接回复
            for(Comment topReply : topReplys) {//topReply是顶层评论的每一个直接回复
                //循环迭代，找出子代，存放在tempReplys中
                dfsAdjustment(topReply);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

   /**
    * 功能描述: dfs将一个评论以及其所有的子评论放到一个数组中
    * @Param: [comment 顶部评论]
    * @Return: void
    * @Author: Sichengluis
    * @Date: 2021/2/5 15:34
    */
    private void dfsAdjustment(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    dfsAdjustment(reply);
                }
            }
        }
    }
}
