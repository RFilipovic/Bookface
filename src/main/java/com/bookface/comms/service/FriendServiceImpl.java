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
        token = parseToken(token);
        Friend friend = createFriendRequestOnValidData(friendRequest, token);
        friendRepository.save(friend);
    }

    private String parseToken(String token){
        return token.substring(7);
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
        token = parseToken(token);
        updateFriendRequestOnValidData(updateRequest, token);
    }

    private void updateFriendRequestOnValidData(CreateFriendRequestUpdate updateRequest, String token){
        validateUpdateRequest(updateRequest);
        if(updateRequest.getAction().equals(FriendRequestAction.ACCEPT))
            enterAcceptProcedure(updateRequest,token);
        else
            enterRejectProcedure(updateRequest, token);
    }

    private void validateUpdateRequest(CreateFriendRequestUpdate updateRequest){
        if(updateRequest == null || updateRequest.getSenderId() == null || updateRequest.getAction() == null)
            throw new ApiRequestException("Invalid update on the friend request.");
    }

    private void enterAcceptProcedure(CreateFriendRequestUpdate updateRequest, String token){
        List<Friend> friendRequests = getAllUserFriendRequests(jwtService.extractUserId(token));
        acceptFriendRequest(updateRequest.getSenderId(), friendRequests);
    }

    private List<Friend> getAllUserFriendRequests(Long userId){
        return friendRepository.findByRecipientId(userId)
                .orElseThrow(() -> new ApiRequestException("No friend requests to fetch."));
    }

    private void acceptFriendRequest(Long senderId, List<Friend> friendRequests){
        for(Friend friendRequest : friendRequests){
            if(Objects.equals(friendRequest.getSenderId(), senderId)) {
                updateRequestStatus(friendRequest);
                return;
            }
        }
        throw new ApiRequestException("Searched friend request does not exist.");
    }

    private void updateRequestStatus(Friend friendRequest){
        friendRequest.setStatus(RequestStatus.ACCEPTED);
        friendRepository.save(friendRequest);
    }

    private void enterRejectProcedure(CreateFriendRequestUpdate updateRequest, String token){
        List<Friend> friendRequests = getAllUserFriendRequests(jwtService.extractUserId(token));
        rejectFriendRequest(updateRequest.getSenderId(), friendRequests);
    }

    private void rejectFriendRequest(Long senderId, List<Friend> friendRequests){
        for(Friend friendRequest : friendRequests){
            if(Objects.equals(friendRequest.getSenderId(), senderId)) {
                friendRepository.delete(friendRequest);
                return;
            }
        }
        throw new ApiRequestException("Searched friend request does not exist.");
    }

}
