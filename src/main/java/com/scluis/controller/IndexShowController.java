package com.scluis.controller;

import com.scluis.config.paraConfig;
import com.scluis.po.Blog;
import com.scluis.service.blogService;
import com.scluis.service.tagService;
import com.scluis.service.typeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by Sichengluis on 2020/12/27 14:08
 */
@Controller
public class IndexShowController {

    @Autowired
    private blogService blogService;
    @Autowired
    private typeService typeService;
    @Autowired
    private tagService tagService;

    private static final int pageSize=3;
    private static final int searchPageSize=1;
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    /**
     * 功能描述: 查看博客详情
     * @Param: [id, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/5 21:30
     */
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getHtmlContentBlog(id));
        return "blog";
    }
    /**
     * 功能描述: 显示博客主界面
     * @Param: [pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/5 21:30
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = pageSize,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<Blog> blogList = blogService.getBlogList(pageable, null);
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("page",blogList);
        model.addAttribute("topTypes",typeService.getTypeList(paraConfig.topTypesPageSize));
        model.addAttribute("topTags",tagService.getTagList(paraConfig.topTagsPageSize));
        model.addAttribute("recentRecommendedBlogs",blogService.getBlogList(paraConfig.recentRecommendedBlogsPageSize));
        model.addAttribute("hotBlogs",blogService.getBlogList(paraConfig.hotBlogsPageSize,"viewTimes"));
        model.addAttribute("newBlogs",blogService.getBlogList(paraConfig.newBlogsPageSize,"createTime"));
        return "index";
    }

    /**
     * 功能描述: 换页
     * @Param:
     * @Return:
     * @Author: Sichengluis
     * @Date: 2021/2/6 18:29
     */
    @PostMapping("/changePage")
    public String changePage(@PageableDefault(size = pageSize,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        Page<Blog> blogList = blogService.getBlogList(pageable, null);
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("page",blogList);
        return "index :: blogList";
    }
    /**
     * 功能描述: 博客全局搜索
     * @Param: [pageable, model, globalQueryKey]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/5 21:30
     */
    @RequestMapping("/search")
    public String search(@PageableDefault(size = searchPageSize,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         Model model,String globalQueryKey){
        Page<Blog> blogList = blogService.getBlogList("%" + globalQueryKey + "%", pageable);
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("page",blogList);
        model.addAttribute("globalQueryKey",globalQueryKey);//为了在查询结果显示页面知道查询的关键字是什么
        model.addAttribute("topTypes",typeService.getTypeList(paraConfig.topTypesPageSize));
        model.addAttribute("topTags",tagService.getTagList(paraConfig.topTagsPageSize));
        model.addAttribute("recentRecommendedBlogs",blogService.getBlogList(paraConfig.recentRecommendedBlogsPageSize));
        model.addAttribute("hotBlogs",blogService.getBlogList(paraConfig.hotBlogsPageSize,"viewTimes"));
        model.addAttribute("newBlogs",blogService.getBlogList(paraConfig.newBlogsPageSize,"createTime"));
        return "search";
    }

    /**
     * 功能描述: 全局搜索换页
     * @Param: [pageable, model, globalQueryKey]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/15 19:52
     */
    @RequestMapping("/searchChangePage")
    public String searchChangePage(@PageableDefault(size = searchPageSize,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         Model model,String globalQueryKey){
        Page<Blog> blogList = blogService.getBlogList("%" + globalQueryKey + "%", pageable);
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("page",blogList);
        model.addAttribute("globalQueryKey",globalQueryKey);//为了在查询结果显示页面知道查询的关键字是什么
        return "search::blogList";
    }


    /**
     * 功能描述: 底部的最新博客
     * @Param: [model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/14 13:30
     */
//    @RequestMapping("/footer/newBlogs")
//    public String newBlogs(Model model){
//        List<Blog> newBlogs = blogService.getBlogList(2, "updateTime");
//        model.addAttribute("newBlogs",newBlogs);
//        return "_fragments::newBlogList";
//    }
}






