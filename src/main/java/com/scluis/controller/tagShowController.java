package com.scluis.controller;

import com.scluis.config.paraConfig;
import com.scluis.po.Blog;
import com.scluis.service.blogService;
import com.scluis.service.tagService;
import com.scluis.service.typeService;
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

import java.util.Locale;
import java.util.ResourceBundle;

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
        // 取得系统默认的国家语言环境
        Locale lc = Locale.getDefault();
        // 根据国家语言环境加载资源文件
        ResourceBundle rb = ResourceBundle.getBundle("i18n/messages", lc);
        // 从资源文件中取得服务器信息
        String showNum = rb.getString("tagShowNum");
        Integer tagShowNum=Integer.valueOf(showNum);
        Page<Blog> blogList = blogService.getBlogList(id,pageable);
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("tags",tagService.getTags(tagShowNum));//一开始只显示tagShowNum个标签
        model.addAttribute("totalTagNum",tagService.getTagList().size());//总共有多少个标签
        model.addAttribute("currentTagNum",tagShowNum);//现在显示了多少个
        model.addAttribute("page",blogList);
        model.addAttribute("activeTagId",id);
        model.addAttribute("topTypes",typeService.getTypeList(paraConfig.topTypesPageSize));
        model.addAttribute("topTags",tagService.getTagList(paraConfig.topTagsPageSize));
        model.addAttribute("recentRecommendedBlogs",blogService.getBlogList(paraConfig.recentRecommendedBlogsPageSize));
        model.addAttribute("hotBlogs",blogService.getBlogList(paraConfig.hotBlogsPageSize,"viewTimes"));
        model.addAttribute("newBlogs",blogService.getBlogList(paraConfig.newBlogsPageSize,"createTime"));
        return "tags";
    }

    /**
     * 功能描述: 分页获取每个标签的所有博客，用于选中某一个标签或者翻页时
     * @Param: [id 标签id, tagNum 页面中显示的tag数量 pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/7 20:30
     */
    @PostMapping("/query")
    public String changePage( Long id,Integer tagNum,@PageableDefault(size = 3,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                              Model model){
        // 取得系统默认的国家语言环境
        Locale lc = Locale.getDefault();
        // 根据国家语言环境加载资源文件
        ResourceBundle rb = ResourceBundle.getBundle("i18n/messages", lc);
        // 从资源文件中取得服务器信息
        String showNum = rb.getString("tagShowNum");
        Integer tagShowNum=Integer.valueOf(showNum);
        Page<Blog> blogList = blogService.getBlogList(id,pageable);//分页查询某一标签的全部博客
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        if(tagNum==tagShowNum){
            //页面只显示了部分分类标签
            model.addAttribute("tags",tagService.getTags(tagShowNum));
            model.addAttribute("currentTagNum",tagShowNum);//现在显示了多少个
        }
        else {
            //页面显示了全部分类标签
            model.addAttribute("tags",tagService.getTagList());
            model.addAttribute("currentTagNum",tagService.getTagList().size());
        }
        model.addAttribute("totalTagNum",tagService.getTagList().size());
        model.addAttribute("page",blogList);
        model.addAttribute("activeTagId",id);
        return "tags :: blogList";
    }
    @RequestMapping("/showOrHide")
    public String showOrHide( Integer num,Integer id,Model model){
        // 取得系统默认的国家语言环境
        Locale lc = Locale.getDefault();
        // 根据国家语言环境加载资源文件
        ResourceBundle rb = ResourceBundle.getBundle("i18n/messages", lc);
        // 从资源文件中取得服务器信息
        String showNum = rb.getString("tagShowNum");
        Integer tagShowNum=Integer.valueOf(showNum);
        model.addAttribute("totalTagNum",tagService.getTagList().size());
        if(id!=null){
            model.addAttribute("activeTagId",id);
        }
        if(num==1){
            //显示全部
            model.addAttribute("tags",tagService.getTagList());
            model.addAttribute("currentTagNum",tagService.getTagList().size());
            return "tags::tagList";
        }
        else if (num == 0){
            model.addAttribute("tags",tagService.getTags(tagShowNum));
            model.addAttribute("currentTagNum",tagShowNum);//现在显示了多少个
            return "tags::tagList";
        }
        return null;
    }
}
