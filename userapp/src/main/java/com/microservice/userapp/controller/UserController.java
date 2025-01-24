package com.microservice.userapp.controller;

import com.microservice.userapp.model.User;
import com.microservice.userapp.repo.UserRepository;
import com.microservice.userapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/getAllUser")
    public List<User> getUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/getUserById/{id}")
    public User getUsers(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
