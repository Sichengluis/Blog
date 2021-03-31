package com.scluis.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sichengluis on 2020/12/27 14:58
 */
@ResponseStatus(HttpStatus.NOT_FOUND)//加上这个注解SpringBoot才会把此异常处理后转发到404页面
public class notFoundException extends RuntimeException {//继承RuntimeException我们才可以捕获

    public notFoundException(String message) {
        super(message);
    }

    public notFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public notFoundException(){

    }
}
