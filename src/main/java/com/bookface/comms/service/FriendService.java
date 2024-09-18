package com.bookface.comms.service;

import com.bookface.comms.service.request.CreateFriendRequest;
import com.bookface.comms.service.request.CreateFriendRequestUpdate;

public interface FriendService {
    void sendFriendRequest(CreateFriendRequest friendRequest, String token);
    void updateFriendRequest(CreateFriendRequestUpdate updateRequest, String token);
}
