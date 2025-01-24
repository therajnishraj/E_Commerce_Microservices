package com.microservice.userapp.service;

import com.microservice.userapp.model.User;

import java.util.List;

public interface UserService {
    public List<User> getAllUser();
    public  User createUser(User user);
    public User getUserById(Long id);
}
