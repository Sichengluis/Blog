package com.scluis.controller;

import com.scluis.config.pageSizeConfig;
import com.scluis.service.blogService;
import com.scluis.service.tagService;
import com.scluis.service.typeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Sichengluis on 2021/2/9 21:55
 */
@Controller
@RequestMapping("/archives")
public class archiveShowController {
    @Autowired
    private blogService blogService;

    @Autowired
    private typeService typeService;

    @Autowired
    private tagService tagService;

    @GetMapping("/")
    public String archives(Model model){
        model.addAttribute("archives",blogService.getBlogList());
        model.addAttribute("blogNum",blogService.getBlogNum());
        model.addAttribute("topTypes", typeService.getTypeList(pageSizeConfig.topTypesPageSize));
        model.addAttribute("topTags", tagService.getTagList(pageSizeConfig.topTagsPageSize));
        model.addAttribute("recentRecommendedBlogs",blogService.getBlogList(pageSizeConfig.recentRecommendedBlogsPageSize));
        model.addAttribute("hotBlogs",blogService.getBlogList(pageSizeConfig.hotBlogsPageSize,"viewTimes"));
        model.addAttribute("newBlogs",blogService.getBlogList(pageSizeConfig.newBlogsPageSize,"createTime"));
        return "archives";
    }
}
