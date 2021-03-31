package com.scluis.controller.admin;

import com.scluis.config.pageSizeConfig;
import com.scluis.po.Tag;
import com.scluis.service.tagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by Sichengluis on 2021/1/24 14:29
 */
@Controller
@RequestMapping("/admin/tags")
public class tagController {

    @Autowired
    private tagService tagService;

    /**
     * 功能描述: 分页展示所有标签
     * @Param: [pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:55
     */
    @GetMapping
    //分页显示tag列表，PageableDefault注解指明分页的参数
    public String tags(@PageableDefault(size = pageSizeConfig.adminTagSize,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable
            , Model model){//通过springboot封装好的pageable对象实现分页,注意是org.springframework.data.domain包下
        //把查询出的数据放到model
        model.addAttribute("page",tagService.getTagList(pageable));
        return "admin/tags";
    }

    /**
     * 功能描述: 跳转到修改或新增标签页面
     * @Param: [id, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:55
     */
    @GetMapping({"/toSaveOrUpdateTag/{id}","/toSaveOrUpdateTag/"})
    public String toSaveOrUpdateTag(@PathVariable(required = false)Long id, Model model){
        if(id==null){//新增
            model.addAttribute("tag", new Tag());//因为添加分类时，前台会获取后台校验返回的tag对象，所以这里转发页面也放了一个空对象
        }
        else{
            model.addAttribute("tag",tagService.getTag(id));
        }
        return "admin/saveOrUpdateTag";
    }

    /**
     * 功能描述: 处理添加和修改标签请求
     * @Param: [tag, result, attributes]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:55
     */
    @PostMapping("/saveOrUpdateTag")
    //使用Tag tagName字段的notBlank注解结合Valid注解和BindingResult实现后台的参数校验
    //前台传参封装的对象tag和BindingResult一定要挨着才能进行校验
    public String saveOrUpdateTag(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        Tag tagFind=tagService.getTagByTagName(tag.getTagName());
        if(tagFind!=null){//BindingResult除了可以根据我们设置的属性验证规则验证（如notBlank注解），还可以人为添加错误信息
            result.rejectValue("tagName","nameError","标签名称已经存在");
        }
        if(result.hasErrors()){//为了防止前端禁用js提交空字段，这里进行后端校验(校验tagName是否为空或已存在)
            return "admin/saveOrUpdateTag";//校验失败转发到saveOrUpdateTag页面，并将tag对象携带回去
        }
        //验证成功，插入数据库
        Tag t=tagService.saveTag(tag);//有主键是修改，没有主键是添加
        if(t==null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id,RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
}
