package com.bookface.comms.service;
import com.bookface.comms.security.AuthResponse;
import com.bookface.comms.service.request.CreateLoginRequest;
import com.bookface.comms.service.request.CreateUserRequest;

public interface UserService {
    AuthResponse registerUser(CreateUserRequest userRequest);
    AuthResponse loginUser(CreateLoginRequest loginRequest);
}
