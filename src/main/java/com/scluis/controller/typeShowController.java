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
 * Created by Sichengluis on 2021/2/5 21:07
 */
@RequestMapping("/types")
@Controller
public class typeShowController {
    @Autowired
    private typeService typeService;

    @Autowired
    private blogService blogService;

    @Autowired tagService tagService;
    /**
     * 功能描述: 展示分类页面
     * @Param: [pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/7 20:43
     */
    @GetMapping({"/","/{id}"})
    public String types( @PathVariable(required = false) Long id,@PageableDefault(size = 2,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        Page<Blog> blogList = blogService.getBlogList(pageable, new blogQuery(id));
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("types",typeService.getTypeList());
        model.addAttribute("page",blogList);
        model.addAttribute("activeTypeId",id);
        model.addAttribute("topTypes",typeService.getTypeList(pageSizeConfig.topTypesPageSize));
        model.addAttribute("topTags",tagService.getTagList(pageSizeConfig.topTagsPageSize));
        model.addAttribute("recentRecommendedBlogs",blogService.getBlogList(pageSizeConfig.recentRecommendedBlogsPageSize));
        model.addAttribute("hotBlogs",blogService.getBlogList(pageSizeConfig.hotBlogsPageSize,"viewTimes"));
        model.addAttribute("newBlogs",blogService.getBlogList(pageSizeConfig.newBlogsPageSize,"createTime"));
        return "types";
    }

    /**
     * 功能描述: 分页获取每个分类的所有博客，用于选中某一个分类或者翻页时
     * @Param: [id 分类id, pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/7 20:30
     */
    @RequestMapping("/query")
    public String query( Long id,@PageableDefault(size = 2,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        Page<Blog> blogList = blogService.getBlogList(pageable, new blogQuery(id));
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("types",typeService.getTypeList());
        model.addAttribute("page",blogList);
        model.addAttribute("activeTypeId",id);
        return "types::blogList";
    }
}
