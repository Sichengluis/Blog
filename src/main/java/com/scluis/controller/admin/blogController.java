package com.scluis.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.scluis.config.pageSizeConfig;
import com.scluis.po.Blog;
import com.scluis.po.Tag;
import com.scluis.po.Type;
import com.scluis.po.User;
import com.scluis.service.blogService;
import com.scluis.service.tagService;
import com.scluis.service.typeService;
import com.scluis.utils.fileUtil;
import com.scluis.vo.blogQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Sichengluis on 2021/1/22 21:28
 */
@Controller
@RequestMapping("/admin/blogs")
public class blogController {
    private static final String SaveOrUpdate="admin/saveOrUpdateBlog";
    private static final String List="admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";
    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Value("${web.image-save-path}")
    private String imageSavepath;
//    @Value("${web.image-show-path}")
//    private String imageShowPath;

    @Autowired
    private blogService blogService;
    @Autowired
    private typeService typeService;
    @Autowired
    private tagService tagService;

    /**
     * 功能描述: 分页展示所有博客
     * @Param: [pageable 分页查询请求对象, blogQuery blog视图对象 封装查询条件, model 返回的视图模型]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:31
     */
    @GetMapping
    public String blogs(@PageableDefault(size = pageSizeConfig.adminBlogSize,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        blogQuery blogQuery, Model model){
        model.addAttribute("types",typeService.getTypeList());
        model.addAttribute("page",blogService.getBlogList(pageable,blogQuery));
        return List;
    }
    /**
     * 功能描述: 返回特定查询条件的分页博客列表，用于点击上、下一页或者搜索后重新加载数据
     * @Param: [pageable 分页查询请求对象, blogQuery blog视图对象 封装查询条件, model 返回的视图模型]
     * @Return: java.lang.String 返回blogs页面的blogList片段，而不是转发到页面
     * 如果直接返回页面，那么重新渲染的页面没有我们已经选择的查询条件，所以返回查询到的数据，页面也只重新渲染blogList片段部分，而不是整个页面
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:35
     */
    @PostMapping("/search")
    public String blogsSearch(@PageableDefault(size =  pageSizeConfig.adminBlogSize,sort = {"createTime"},direction = Sort.Direction.DESC) Pageable pageable,
                              blogQuery blogQuery, Model model){
        model.addAttribute("page",blogService.getBlogList(pageable,blogQuery));
        return "admin/blogs::blogList";
    }

    /**
     * 功能描述: 跳转到新增或者修改博客页面
     * @Param: [id 非必须值 代表所有修改博客的id, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:39
     */
    @GetMapping({"/toSaveOrUpdateBlog/{id}","/toSaveOrUpdateBlog/"})
    public String toSaveOrUpdateBlog(@PathVariable(required = false)Long id, Model model){
//        添加或修改页面能选择的所有分类和标签
        model.addAttribute("types",typeService.getTypeList());
        model.addAttribute("tags",tagService.getTagList());
        if(id==null){//新增
            model.addAttribute("blog", new Blog());
        }
        else{
            //拿到blog初始化他的tagIds,tagIds没有存储在数据库，需要后台通过List生成
            Blog blog=blogService.getBlog(id);
            blog.initTagIds();
            model.addAttribute("blog",blog);
        }
        return SaveOrUpdate;
    }

    /**
     * 功能描述: 处理添加和修改博客请求
     * @Param: [session, blog 所处理的博客对象, result 后台校验结果, attributes 重定向到的页面显示的数据]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:40
     */
    @PostMapping("/saveOrUpdateBlog")
    //使用Blog BlogName字段的notBlank注解结合Valid注解和BindingResult实现后台的参数校验
    //前台传参封装的对象Blog和BindingResult一定要挨着才能进行校验
    public String saveOrUpdateBlog(HttpSession session,@Valid Blog blog, BindingResult result, RedirectAttributes attributes){
        //非空校验
        if(result.hasErrors()){
            return SaveOrUpdate;
        }
        //设置博客的标签
        List<Long> tagIdList =new ArrayList<>();
        if(blog.getTagIds()!=null&&!"".equals(blog.getTagIds())){//文章有标签，转化得到标签List
             tagIdList = strToLongList(blog.getTagIds());
        }
        blog.setTags(tagService.getTagList(tagIdList));
        //设置博客的分类
        String typeName=blog.getType().getTypeName();
        Type typeFind=typeService.getTypeByTypeName(typeName);
        if(typeFind==null){//用户提交博客时新增加分类
            Type typeNew=new Type(typeName);
            blog.setType(typeService.saveType(typeNew));
        }
        else{
            blog.setType(typeFind);
        }
        //设置博客的user
        blog.setUser((User)session.getAttribute("user"));
        Blog b;
        if(blog.getId()==null){
            //新增
           b=blogService.saveBlog(blog);
        }
        else {
            //修改
            b=blogService.updateBlog(blog.getId(),blog);
        }
        //返回操作结果
        if(b==null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return REDIRECT_LIST;
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id,RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }

    /**
     * 功能描述: 将String类型的标签id转换成List数组，如果用户新定义的标签已经存在会返回null
     * @Param: [ids]
     * @Return: java.util.List<java.lang.Long>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:42
     */
    public  List<Long> strToLongList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                //已经存在的标签是id，新添加的标签是名称
                try{//已经存在的标签直接添加
                    list.add(new Long(idarray[i]));
                }
                catch (NumberFormatException exception){//用户新定义的标签
                    Tag tagByTagName = tagService.getTagByTagName(idarray[i]);
                    if(tagByTagName==null){
                        Tag tagSave = tagService.saveTag(new Tag(idarray[i]));
                        list.add(tagSave.getId());
                    }
                    else{
                        return null;
                    }
                }
            }
        }
        return list;
    }

    /**
     * 功能描述: 处理editormd中图片上传
     * @Param: [image, request, response]
     * @Return: com.alibaba.fastjson.JSONObject
     * @Author: Sichengluis
     * @Date: 2021/2/20 16:45
     */
    @RequestMapping("/editormdPicUpload")
    @ResponseBody
    public JSONObject editormdPicUpload(@RequestParam(value = "editormd-image-file",required = false)MultipartFile image) throws IOException{
        JSONObject jsonObject=new JSONObject();
        try{
            String url = "/upload-images/"+fileUtil.save(image, imageSavepath, image.getOriginalFilename());
            jsonObject.put("success",1);
            jsonObject.put("message","上传成功~");
            jsonObject.put("url",url);

        }
        catch (Exception e){
            jsonObject.put("success",0);
            jsonObject.put("message","出错啦~");
        }
        return jsonObject;
    }


    @RequestMapping("/firstPicUpload")
    @ResponseBody
    public JSONObject firstPicUpload(@RequestParam(value = "firstPic",required = false)MultipartFile image,
                                        HttpServletRequest request, HttpServletResponse response) throws IOException{
        logger.info("-----------------------------------------");
        logger.info("首图上传失败测试-进入firstPicUpload");
        JSONObject jsonObject=new JSONObject();
        try{
            logger.info("首图上传失败测试-进入try");
            String url = "/upload-images/"+fileUtil.save(image, imageSavepath, image.getOriginalFilename());
            logger.info("首图上传失败测试-得到url，url为："+url);
            jsonObject.put("msg","上传成功~");
            jsonObject.put("url",url);
        }
        catch (Exception e){
            logger.info("首图上传失败测试-firstPicUpload出现异常，异常信息为："+e.toString());
            jsonObject.put("msg","出错啦~");
        }
        return jsonObject;
    }
}
