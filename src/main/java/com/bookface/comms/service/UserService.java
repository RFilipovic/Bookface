package com.bookface.comms.service;
import com.bookface.comms.service.request.CreateLoginRequest;
import com.bookface.comms.service.request.CreateUserRequest;

public interface UserService {
    void addUser(CreateUserRequest userRequest);
    void loginValid(CreateLoginRequest loginRequest);
}
