package com.scluis.service;

import com.scluis.po.User;

/**
 * Created by Sichengluis on 2021/1/20 19:35
 */
public interface userService {
    /**
     * 功能描述: 返回用户名为username，密码为password的用户对象
     * @Param: [username, password]
     * @Return: com.scluis.po.User
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:26
     */
    User checkUser(String username , String password);
}
