package com.bookface.comms.service;
import com.bookface.comms.domain.User;
import com.bookface.comms.security.AuthResponse;
import com.bookface.comms.service.request.CreateLoginRequest;
import com.bookface.comms.service.request.CreateUserRequest;

public interface UserService {
    AuthResponse addUser(CreateUserRequest userRequest);
    AuthResponse loginValid(CreateLoginRequest loginRequest);
    User findByUsername(String username);
}
