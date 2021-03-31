package com.scluis.repository;

import com.scluis.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Sichengluis on 2021/1/20 19:37
 */
//继承JpaRepository后我们会自动得到增删改查方法
public interface userRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
