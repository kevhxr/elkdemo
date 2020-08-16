package com.hxr.elkdemo.service;

import com.hxr.elkdemo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserService {

    void save(User user);

    void deleteById(String id);

    User queryUserById(String id);

    Iterable<User> queryAll();

    Page<User> findByName(String name, PageRequest request);

}
