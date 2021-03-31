package com.scluis.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Sichengluis on 2020/12/27 14:13
 */
//全局异常处理，拦截所有异常，进行统一处理，并返回到不同页面
@ControllerAdvice//拦截所有带Controller注解的控制器
public class exceptionHandler {
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    //@ExceptionHandler 注解用来指明异常的处理类型，只会拦截参数对应的异常
    //Exception是所有异常的父类，所以这里能够拦截所有异常信息
    @ExceptionHandler(Exception.class)
    //返回页面和一些数据信息
    public ModelAndView controllerExceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL:{},Exception:{}",request.getRequestURL(),e);
        //如果是我们自定义的404异常，转到404页面
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class)!=null){//判断一个类是否包含一个注解
            //异常存在手动指定的状态码，我们就不要拦截了，交给springboot本身去处理它
            throw e;
        }
        //其它异常统一处理转到error页面
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("url",request.getRequestURL());
        modelAndView.addObject("exception",e);
        //指定返回到哪个页面
        modelAndView.setViewName("error/error");
        return modelAndView;
    }
}
