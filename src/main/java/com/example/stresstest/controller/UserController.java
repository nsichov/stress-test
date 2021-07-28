package com.example.stresstest.controller;

import com.example.stresstest.entity.User;
import com.example.stresstest.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class UserController {
    private UserRepository userRepository;
    public Map<String, Object> cache = new ConcurrentHashMap<>();
    
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Object all() {
        if (!cache.containsKey("users")) {
            cache.put("users", userRepository.findAll());
        }
        
        return cache.get("users");
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User newUser) {
        return userRepository.save(newUser);
    }

    @Scheduled(fixedRate = 10000)
    public void refreshCache(){
        cache.put("users", userRepository.findAll());
    }
}
