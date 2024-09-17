package com.bookface.comms.service;
import com.bookface.comms.domain.Friend;
import com.bookface.comms.domain.RequestStatus;
import com.bookface.comms.domain.repository.FriendRepository;
import com.bookface.comms.domain.repository.UserRepository;
import com.bookface.comms.exception.ApiRequestException;
import com.bookface.comms.security.JwtService;
import com.bookface.comms.service.request.CreateFriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class FriendServiceImpl implements FriendService{

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public FriendServiceImpl(FriendRepository friendRepository, UserRepository userRepository, JwtService jwtService){
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public void sendFriendRequest(CreateFriendRequest friendRequest, String token) {
        token = parseToken(token);
        validateFriendRequest(friendRequest, token);
        Friend friend = createFriendRequestOnValidData(friendRequest, token);
        friendRepository.save(friend);
    }

    private String parseToken(String token){
        return token.substring(7);
    }

    private void validateFriendRequest(CreateFriendRequest friendRequest, String token){
        checkForSelfRequest(friendRequest, token);
        userExists(friendRequest);
    }

    private void checkForSelfRequest(CreateFriendRequest friendRequest, String token){
        if(Objects.equals(friendRequest.getRecipientId(), jwtService.extractUserId(token)))
            throw new ApiRequestException("Cannot send a friend request to yourself.");
    }

    private void userExists(CreateFriendRequest friendRequest){
        if(userRepository.findByUserId(friendRequest.getRecipientId()).isEmpty())
            throw new ApiRequestException("User does not exist.");
    }

    private Friend createFriendRequestOnValidData(CreateFriendRequest friendRequest, String token){
        return Friend.builder()
                .userId(jwtService.extractUserId(token))
                .friendId(friendRequest.getRecipientId())
                .status(RequestStatus.PENDING)
                .build();
    }
}
