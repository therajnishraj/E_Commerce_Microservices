package com.microservice.userapp.serviceimpl;

import com.microservice.userapp.model.User;
import com.microservice.userapp.repo.UserRepository;
import com.microservice.userapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository repository;

    @Override
    public List<User> getAllUser() {
        return repository.findAll();
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public User getUserById(Long id) {
       Optional<User> user= repository.findById(id);
       if(Objects.isNull(user))
           return new User();
       else
           return user.get();
    }
}
