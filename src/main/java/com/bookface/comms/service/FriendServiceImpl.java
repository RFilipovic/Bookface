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

        if((Objects.equals(friendRequest.getSenderId(), jwtService.extractUserId(token)))
                && userExists(friendRequest.getRecipientId())){

            Friend friend = Friend.builder()
                    .userId(friendRequest.getSenderId())
                    .friendId(friendRequest.getRecipientId())
                    .status(RequestStatus.PENDING)
                    .build();
            friendRepository.save(friend);

        }else throw new ApiRequestException("The user doesn't exist.");
    }

    private boolean userExists(Long senderId) {
        userRepository.findById(senderId)
                .orElseThrow(() ->new ApiRequestException("The user doesn't exist."));
        return true;
    }


}
