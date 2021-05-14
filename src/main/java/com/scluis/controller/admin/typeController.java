package com.scluis.controller.admin;

import com.scluis.config.paraConfig;
import com.scluis.po.Type;
import com.scluis.service.typeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;


/**
 * Created by Sichengluis on 2021/1/23 10:25
 */
@Controller
@RequestMapping("/admin/types")
public class typeController {
    @Autowired
    private typeService typeService;

    /**
     * 功能描述: 分页显示type列表，PageableDefault注解指明分页的参数
     * @Param: [pageable, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:55
     */
    @GetMapping
    public String types(@PageableDefault(size = paraConfig.adminTypeSize,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable
    , Model model){//通过springboot封装好的pageable对象实现分页,注意是org.springframework.data.domain包下
        //把查询出的数据放到model
        model.addAttribute("page",typeService.getTypeList(pageable));
        return "admin/types";
    }

    /**
     * 功能描述: 转发到添加和修改分类页面
     * @Param: [id, model]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:56
     */
    @GetMapping({"/toSaveOrUpdateType/{id}","/toSaveOrUpdateType/"})
    public String toSaveOrUpdateType(@PathVariable(required = false)Long id, Model model){
        if(id==null){//新增
            model.addAttribute("type", new Type());//因为添加分类时，前台会获取后台校验返回的type对象，所以这里转发页面也放了一个空对象
        }
        else{
            model.addAttribute("type",typeService.getType(id));
        }
        return "admin/saveOrUpdateType";
    }

    /**
     * 功能描述:处理添加和修改分类请求
     * @Param: [type, result, attributes]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:55
     */
    @PostMapping("/saveOrUpdateType")
    //使用Type typeName字段的notBlank注解结合Valid注解和BindingResult实现后台的参数校验
    //前台传参封装的对象type和BindingResult一定要挨着才能进行校验
    public String saveOrUpdateType(@Valid Type type, BindingResult result, RedirectAttributes attributes){

        Type typeFind=typeService.getTypeByTypeName(type.getTypeName());
        if(typeFind!=null){//BindingResult除了可以根据我们设置的属性验证规则验证（如notBlank注解），还可以人为添加错误信息
            result.rejectValue("typeName","nameError","分类名称已经存在");
        }
        if(result.hasErrors()){//为了防止前端禁用js提交空字段，这里进行后端校验(校验typeName是否为空或已存在)
            return "admin/saveOrUpdateType";//校验失败转发到saveOrUpdateType页面，并将type对象携带回去
        }
        //验证成功，插入数据库
        Type t;
        t=typeService.saveType(type);
        if(t==null){
            attributes.addFlashAttribute("message","操作失败");
        }else{
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/types";
    }

    /**
     * 功能描述: 处理删除分类请求
     * @Param: [id, attributes]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:56
     */
    @GetMapping("/delete/{id}")
    public String deleteType(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}
