package com.bookface.comms.service;
import com.bookface.comms.domain.User;
import com.bookface.comms.security.AuthenticationResponse;
import com.bookface.comms.service.request.CreateLoginRequest;
import com.bookface.comms.service.request.CreateUserRequest;

public interface UserService {
    AuthenticationResponse addUser(CreateUserRequest userRequest);
    AuthenticationResponse loginValid(CreateLoginRequest loginRequest);
    User findByUsername(String username);
}
