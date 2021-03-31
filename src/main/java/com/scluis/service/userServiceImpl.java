package com.scluis.service;

import com.scluis.po.User;
import com.scluis.repository.userRepository;
import com.scluis.utils.md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Sichengluis on 2021/1/20 19:35
 */
@Service
public class userServiceImpl implements userService {

    @Autowired
    private userRepository userRepository;
    @Override
    public User checkUser(String username, String password) {
        User user= userRepository.findByUsernameAndPassword(username, md5.code(password));
        return user;
    }
}
