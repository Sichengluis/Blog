package com.scluis.controller;

import com.scluis.config.pageSizeConfig;
import com.scluis.po.Blog;
import com.scluis.service.blogService;
import com.scluis.service.tagService;
import com.scluis.service.typeService;
import com.scluis.vo.blogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sichengluis on 2021/2/8 20:02
 */

@Controller
@RequestMapping("/tags")
public class tagShowController {
    @Autowired
    private tagService tagService;

    @Autowired
    private blogService blogService;

    @Autowired
    private typeService typeService;

    /**
     * 功能描述: 展示标签页面
     * @Param: [pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/7 20:43
     */
    @GetMapping({"/","/{id}"})
    public String tags(@PathVariable(required = false) Long id, @PageableDefault(size = 3,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       Model model){
        Page<Blog> blogList = blogService.getBlogList(id,pageable);
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("tags",tagService.getTagList());
        model.addAttribute("page",blogList);
        model.addAttribute("activeTagId",id);
        model.addAttribute("topTypes",typeService.getTypeList(pageSizeConfig.topTypesPageSize));
        model.addAttribute("topTags",tagService.getTagList(pageSizeConfig.topTagsPageSize));
        model.addAttribute("recentRecommendedBlogs",blogService.getBlogList(pageSizeConfig.recentRecommendedBlogsPageSize));
        model.addAttribute("hotBlogs",blogService.getBlogList(pageSizeConfig.hotBlogsPageSize,"viewTimes"));
        model.addAttribute("newBlogs",blogService.getBlogList(pageSizeConfig.newBlogsPageSize,"createTime"));
        return "tags";
    }

    /**
     * 功能描述: 分页获取每个标签的所有博客，用于选中某一个标签或者翻页时
     * @Param: [id 标签id, pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/7 20:30
     */
    @PostMapping("/query")
    public String changePage( Long id,@PageableDefault(size = 3,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                              Model model){
        Page<Blog> blogList = blogService.getBlogList(id,pageable);//分页查询某一标签的全部博客
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("tags",tagService.getTagList());
        model.addAttribute("page",blogList);
        model.addAttribute("activeTagId",id);
        return "tags :: blogList";
    }
}
