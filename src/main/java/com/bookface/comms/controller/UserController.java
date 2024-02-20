package com.bookface.comms.controller;

import com.bookface.comms.service.UserService;
import com.bookface.comms.service.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody CreateUserRequest userRequest){
        userService.addUser(userRequest);
        return ResponseEntity.ok("Account successfully created!");
    }
}
