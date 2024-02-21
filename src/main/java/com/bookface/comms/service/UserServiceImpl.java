package com.bookface.comms.service;
import com.bookface.comms.domain.User;
import com.bookface.comms.domain.repository.UserRepository;
import com.bookface.comms.exception.ApiRequestException;
import com.bookface.comms.service.request.CreateUserRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(CreateUserRequest userRequest) {
        validateUserRequest(userRequest);
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);
    }

    private void validateUserRequest(CreateUserRequest userRequest) {
        if (userRequest == null || StringUtils.isAnyBlank(userRequest.getName(), userRequest.getEmail(), userRequest.getPassword())) {
            throw new ApiRequestException("Invalid registration request, try again.");
        }
    }
}
