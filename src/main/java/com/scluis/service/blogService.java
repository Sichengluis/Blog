package com.scluis.service;

import com.scluis.po.Blog;
import com.scluis.vo.blogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by Sichengluis on 2021/1/24 16:06
 */
public interface blogService {
    Blog getBlog(Long id);
    Blog getHtmlContentBlog(Long id);
    /**
     * 功能描述: 分页获取博客列表
     * @Param: [pageable 分页查询对象, blogQuery Blog的视图对象，封装了查询条件]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:16
     */
    Page<Blog> getBlogList(Pageable pageable,blogQuery blogQuery);
    /**
     * 功能描述: 根据sortBy属性排序，返回前size条博客，用于获取最新推荐或热门文章
     * @Param: [size, sortBy]
     * @Return: java.util.List<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:17
     */
    List<Blog> getBlogList(Integer size,String sortBy);
    /**
     * 功能描述: 根据globalQueryKey作为关键字，分页查询匹配的博客列表，用于全局搜索博客
     * @Param: [globalQueryKey 全局搜索关键字, pageable]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:17
     */
    Page<Blog> getBlogList(String globalQueryKey,Pageable pageable);

    /**
     * 功能描述: 查询某一标签的所有博客
     * @Param: [tagId, pageable]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/8 20:07
     */
    Page<Blog> getBlogList(Long tagId,Pageable pageable);

    /**
     * 功能描述: 返回每个年份 及该年份的博客
     * @Param: []
     * @Return: java.util.Map<java.lang.String,java.util.List<com.scluis.po.Blog>>
     * @Author: Sichengluis
     * @Date: 2021/2/10 13:27
     */
    Map<String,List<Blog>>getBlogList();

    /**
     * 功能描述: 返回最新被推荐的size条博客
     * @Param: [size]
     * @Return: java.util.List<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/10 14:48
     */
    List<Blog> getBlogList(Integer size);

    /**
     * 功能描述: 获取一共有多少条博客
     * @Param: []
     * @Return: java.lang.Long
     * @Author: Sichengluis
     * @Date: 2021/2/10 13:40
     */
    Long getBlogNum();
    Blog saveBlog(Blog blog);
    Blog updateBlog(Long id,Blog blog);
    void deleteBlog(Long id);

}
