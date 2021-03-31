package com.scluis.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by Sichengluis on 2020/12/27 15:35
 */
@Aspect
@Component//采用aop记录每个请求的请求url，访问者ip，调用的方法，传入的参数和返回的内容
public class logAspect {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Pointcut("execution(* com.scluis.controller.*.*(..))")//定义一个切面，切面拦截web包下所有类的所有方法
    public void log(){//一个空方法加上Pointcut注解就是一个切面

    }
    //在方法调用之前，记录请求的信息，包括参数，请求的url，调用的方法和请求者ip
    @Before("log()")//定义方法在切面之前执行，参数指明是哪个切面
    public void doBefore(JoinPoint joinPoint){
        //通过RequestContextHolder拿到servletRequestAttributes从而获取到请求的HttpServletRequest
        ServletRequestAttributes servletRequestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=servletRequestAttributes.getRequest();
        //从request中获取到请求ip和url
        String ip=request.getRemoteAddr();
        String url=request.getRequestURL().toString();
        //通过joinPoint拿到拦截的类名、方法名和参数
        String classMethod=joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName();
        Object[]args=joinPoint.getArgs();
        requestLog requestLog=new requestLog(url,ip,classMethod,args);
        logger.info("Request:{}",requestLog);
    }

//    @After("log()")//定义方法在切面之后执行
//    public void doAfter(){
//        logger.info("-----------doAfter---------------");
//    }

    //在方法返回值之后执行，记录方法的返回值
    @AfterReturning(returning = "res",pointcut = "log()")//returning的值和方法参数相对应
    public void doAfterReturn(Object res){//参数是方法返回值
        logger.info("Result:{}",res);
    }

    //内部类
    private class requestLog{
        private String url;
        private String ip;
        private String classMethod;
        private Object[]args;
        public requestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }
        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }

}
