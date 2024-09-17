package com.bookface.comms.service;
import com.bookface.comms.domain.Role;
import com.bookface.comms.domain.User;
import com.bookface.comms.domain.repository.UserRepository;
import com.bookface.comms.exception.ApiRequestException;
import com.bookface.comms.security.AuthResponse;
import com.bookface.comms.security.JwtService;
import com.bookface.comms.service.request.CreateLoginRequest;
import com.bookface.comms.service.request.CreateUserRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse registerUser(CreateUserRequest userRequest) {
        User user = createUserOnValidRequest(userRequest);
        userRepository.save(user);
        return getAuthToken(user);
    }

    public User createUserOnValidRequest(CreateUserRequest userRequest) {
        validateUserRequest(userRequest);
        return createUserObject(userRequest);
    }

    private void validateUserRequest(CreateUserRequest userRequest) {
        if (userRequest == null || StringUtils.isAnyBlank(userRequest.getUsername(), userRequest.getEmail(), userRequest.getPassword()))
            throw new ApiRequestException("All fields must be filled in the registration form.");
    }

    public User createUserObject (CreateUserRequest userRequest){
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(Role.USER);
        return user;
    }

    private AuthResponse getAuthToken(User user) {
        String jwtToken = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Override
    public AuthResponse loginUser(CreateLoginRequest loginRequest) {
        User user = getUserOnAuthenticatedLoginRequest(loginRequest);
        return getAuthToken(user);
    }

    private User getUserOnAuthenticatedLoginRequest(CreateLoginRequest loginRequest){
        authenticateLoginRequest(loginRequest);
        return userRepository
                .findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ApiRequestException("Username does not exist."));
    }

    private void authenticateLoginRequest(CreateLoginRequest loginRequest) {
        validateLoginRequest(loginRequest);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
    }

    public void validateLoginRequest(CreateLoginRequest loginRequest){
        if(loginRequest == null || StringUtils.isAnyBlank(loginRequest.getUsername(), loginRequest.getPassword()))
            throw new ApiRequestException("All fields must be filled in the login form.");
    }

}
