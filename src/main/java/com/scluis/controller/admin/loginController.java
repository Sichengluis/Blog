package com.scluis.controller.admin;

import com.scluis.po.User;
import com.scluis.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Sichengluis on 2021/1/20 19:44
 */
@Controller
@RequestMapping("/admin")
public class loginController {
    @Autowired
    private userService userService;

    /**
     * 功能描述: 跳转到登陆页面
     * @Param: []
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:53
     */
    @GetMapping//方法不写路径则匹配类上的路径
    public String toLogin(){
        //转发的路径默认在templates下
        return "admin/login";
    }

    /**
     * 功能描述: 处理登陆请求
     * @Param: [request, username, password, session, attributes]
     * @Return: java.lang.String
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:54
     */
    @RequestMapping("/login")
    //使用RequestParam注解，请求的key可以和方法的参数名不一致
//    @RequestParam(value = "username",required = false)//如果不设置required为false那么用户直接输入url访问/admin/login会报缺失参数的异常
    //@RequestParam(value = "password",required = false)
    public String login(HttpServletRequest request, String username,  String password, HttpSession session, RedirectAttributes attributes){
        if(request.getMethod().equals("GET")){//拦截用户直接输入url访问登录接口
            attributes.addFlashAttribute("message","请先进行登录");//通过RedirectAttributes给重定向到的页面传递信息
            return "redirect:/admin";
        }
        User user=userService.checkUser(username,password);
        if(user!=null){
            //用户名密码正确
            user.setPassword(null);//不要把用户的密码存到session
            session.setAttribute("user",user);
            //return "admin/managerIndex";
            return "redirect:/admin/blogs";
        }
        else {
            attributes.addFlashAttribute("message","用户名或密码错误");//通过RedirectAttributes给重定向到的页面传递信息
            return "redirect:/admin";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
