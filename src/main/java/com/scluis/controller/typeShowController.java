package com.scluis.controller;

import com.scluis.config.paraConfig;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;
import java.util.ResourceBundle;

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
    public String types( @PathVariable(required = false) Long id,@PageableDefault(size = 3,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        // 取得系统默认的国家语言环境
        Locale lc = Locale.getDefault();
        // 根据国家语言环境加载资源文件
        ResourceBundle rb = ResourceBundle.getBundle("i18n/messages", lc);
        // 从资源文件中取得服务器信息
        String num = rb.getString("typeShowNum");
        Integer typeShowNum=Integer.valueOf(num);
        Page<Blog> blogList = blogService.getBlogList(pageable, new blogQuery(id));
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        model.addAttribute("types",typeService.getTypes(typeShowNum));//一开始只显示typeShowNum个分类标签
        model.addAttribute("totalTypeNum",typeService.getTypeList().size());//总共有多少个分类
        model.addAttribute("currentTypeNum",typeShowNum);//现在显示了多少个
        model.addAttribute("page",blogList);
        model.addAttribute("activeTypeId",id);
        model.addAttribute("topTypes",typeService.getTypeList(paraConfig.topTypesPageSize));
        model.addAttribute("topTags",tagService.getTagList(paraConfig.topTagsPageSize));
        model.addAttribute("recentRecommendedBlogs",blogService.getBlogList(paraConfig.recentRecommendedBlogsPageSize));
        model.addAttribute("hotBlogs",blogService.getBlogList(paraConfig.hotBlogsPageSize,"viewTimes"));
        model.addAttribute("newBlogs",blogService.getBlogList(paraConfig.newBlogsPageSize,"createTime"));
        return "types";
    }

    /**
     * 功能描述: 分页获取每个分类的所有博客，用于选中某一个分类或者翻页时
     * @Param: [id 分类id, typeNum 页面中显示的分类的数量, pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/7 20:30
     */
    @RequestMapping("/query")
    public String query( Long id,Integer typeNum,@PageableDefault(size = 3,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        // 取得系统默认的国家语言环境
        Locale lc = Locale.getDefault();
        // 根据国家语言环境加载资源文件
        ResourceBundle rb = ResourceBundle.getBundle("i18n/messages", lc);
        // 从资源文件中取得服务器信息
        String num = rb.getString("typeShowNum");
        Integer typeShowNum=Integer.valueOf(num);
        Page<Blog> blogList = blogService.getBlogList(pageable, new blogQuery(id));
        //初始化每个博客的内容简介
        for (Blog blog:blogList) {
            blog.initDescription();
        }
        if(typeNum==typeShowNum){
            //页面只显示了部分分类标签
            model.addAttribute("types",typeService.getTypes(typeShowNum));
            model.addAttribute("currentTypeNum",typeShowNum);//现在显示了多少个
        }
        else {
            //页面显示了全部分类标签
            model.addAttribute("types",typeService.getTypeList());
            model.addAttribute("currentTypeNum",typeService.getTypeList().size());
        }
        model.addAttribute("totalTypeNum",typeService.getTypeList().size());
        model.addAttribute("page",blogList);
        model.addAttribute("activeTypeId",id);
        return "types::blogList";
    }

    /**
     * 分类页面的分类标签显示全部和隐藏
     * @param num 1代表显示全部，0代表隐藏
     * @param model
     * @return
     */
    @RequestMapping("/showOrHide")
    public String showOrHide( Integer num,Integer id,Model model){
        // 取得系统默认的国家语言环境
        Locale lc = Locale.getDefault();
        // 根据国家语言环境加载资源文件
        ResourceBundle rb = ResourceBundle.getBundle("i18n/messages", lc);
        // 从资源文件中取得服务器信息
        String showNum = rb.getString("typeShowNum");
        Integer typeShowNum=Integer.valueOf(showNum);
        model.addAttribute("totalTypeNum",typeService.getTypeList().size());
        if(id!=null){
            model.addAttribute("activeTypeId",id);
        }
       if(num==1){
           //显示全部
           model.addAttribute("types",typeService.getTypeList());
           model.addAttribute("currentTypeNum",typeService.getTypeList().size());
           return "types::typeList";
       }
       else if (num == 0){
           model.addAttribute("types",typeService.getTypes(typeShowNum));
           model.addAttribute("currentTypeNum",typeShowNum);//现在显示了多少个
           return "types::typeList";
       }
       return null;
    }
}
