package com.bookface.comms.service;
import com.bookface.comms.domain.Friend;
import com.bookface.comms.domain.FriendRequestAction;
import com.bookface.comms.domain.RequestStatus;
import com.bookface.comms.domain.repository.FriendRepository;
import com.bookface.comms.domain.repository.UserRepository;
import com.bookface.comms.exception.ApiRequestException;
import com.bookface.comms.security.JwtService;
import com.bookface.comms.service.request.CreateFriendRequest;
import com.bookface.comms.service.request.CreateFriendRequestUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
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
        friendRepository.save(createFriendRequestOnValidData(
                friendRequest,
                token.substring(7))
        );
    }

    private Friend createFriendRequestOnValidData(CreateFriendRequest friendRequest, String token){
        validateFriendRequest(friendRequest, token);
        return Friend.builder()
                .senderId(jwtService.extractUserId(token))
                .recipientId(friendRequest.getRecipientId())
                .status(RequestStatus.PENDING)
                .build();
    }

    private void validateFriendRequest(CreateFriendRequest friendRequest, String token){
        checkFriendRequest(friendRequest);
        checkForSelfRequest(friendRequest, token);
        checkUserExistence(friendRequest);
    }

    private void checkFriendRequest(CreateFriendRequest friendRequest){
        if(friendRequest == null || Objects.equals(friendRequest.getRecipientId(), null))
            throw new ApiRequestException("Invalid friend request");
    }

    private void checkForSelfRequest(CreateFriendRequest friendRequest, String token){
        if(Objects.equals(friendRequest.getRecipientId(), jwtService.extractUserId(token)))
            throw new ApiRequestException("Cannot send a friend request to yourself.");
    }

    private void checkUserExistence(CreateFriendRequest friendRequest){
        if(userRepository.findByUserId(friendRequest.getRecipientId()).isEmpty())
            throw new ApiRequestException("User does not exist.");
    }

    @Override
    public void updateFriendRequest(CreateFriendRequestUpdate updateRequest, String token) {
        updateFriendRequestOnValidData(updateRequest, token.substring(7));
    }

    private void updateFriendRequestOnValidData(CreateFriendRequestUpdate updateRequest, String token){
        validateUpdateRequest(updateRequest);
        if(updateRequest.getAction().equals(FriendRequestAction.ACCEPT))
            accept(updateRequest,token);
        else
            reject(updateRequest, token);
    }

    private void validateUpdateRequest(CreateFriendRequestUpdate updateRequest){
        if(updateRequest == null || updateRequest.getSenderId() == null || updateRequest.getAction() == null)
            throw new ApiRequestException("Invalid update on the friend request.");
    }

    private void accept(CreateFriendRequestUpdate requestUpdate, String token){
        updateRequestStatus(parseFriendRequest(
                requestUpdate.getSenderId(),
                getAllUserFriendRequests(jwtService.extractUserId(token))
        ));
    }

    private Friend parseFriendRequest(Long senderId, List<Friend> friendRequests){
        for(Friend friendRequest : friendRequests){
            if(Objects.equals(friendRequest.getSenderId(), senderId)) {
                return friendRequest;
            }
        }
        throw new ApiRequestException("Searched friend request does not exist.");
    }

    private List<Friend> getAllUserFriendRequests(Long userId){
        return friendRepository.findByRecipientId(userId)
                .orElseThrow(() -> new ApiRequestException("No friend requests to fetch."));
    }

    private void updateRequestStatus(Friend friendRequest){
        friendRequest.setStatus(RequestStatus.ACCEPTED);
        friendRepository.save(friendRequest);
    }

    private void reject(CreateFriendRequestUpdate updateRequest, String token){
        friendRepository.delete(parseFriendRequest(
                updateRequest.getSenderId(),
                getAllUserFriendRequests(jwtService.extractUserId(token))
        ));
    }

}
