package com.daocloud.impl;

import com.daocloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * 必须实现的接口 否则 系统会给出一个默认的密码
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserService userService;

    /**
     * 验证用户是否存在 成功返回其权限
     * 可以根据username 实现应用成面的的用户认证 如无需认证 则可以写死password password是必须存在的
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        com.daocloud.entity.User userMap = userService.getUserInfoByUsername(username);
        if(userMap!=null) {
            return userMap;
        }else {
            return null;
        }
    }


}
