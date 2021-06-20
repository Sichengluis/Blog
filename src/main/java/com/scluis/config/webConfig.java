package com.scluis.config;

import com.scluis.interceptor.loginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Sichengluis on 2021/1/23 9:42
 */
@Configuration
public class webConfig extends WebMvcConfigurerAdapter {

    @Value("${web.image-save-path}")
    private String imageSavePath;

    @Override
    /**
     * 功能描述: 定义拦截器的拦截路径,拦截/admin下除去/admin/login和/admin的所有路径
     * @Param: [registry]
     * @Return: void
     * @Author: Sichengluis
     * @Date: 2021/2/16 20:20
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new loginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin")//不排除这个路径的话，会循环重定向到/admin路径
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/upload-images/**");

    }

    /**
     * 功能描述: 配置扫描static下各个文件夹的静态资源
     * @Param: [registry]
     * @Return: void
     * @Author: Sichengluis
     * @Date: 2021/2/16 20:18
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");//配置浏览器直接访问static下的静态资源
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");//配置浏览器直接访问static下的静态资源
        registry.addResourceHandler("/upload-images/**").addResourceLocations("file:" + imageSavePath);//配置图片物理存储路径和虚拟访问路径
    }

}
