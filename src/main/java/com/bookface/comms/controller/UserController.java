package com.bookface.comms.controller;
import com.bookface.comms.security.AuthResponse;
import com.bookface.comms.service.UserService;
import com.bookface.comms.service.request.CreateLoginRequest;
import com.bookface.comms.service.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody CreateUserRequest userRequest){
        return ResponseEntity.ok(userService.registerUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody CreateLoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

}
