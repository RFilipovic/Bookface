package com.bookface.comms.service;

import com.bookface.comms.service.request.CreateFriendRequest;

public interface FriendService {
    void sendFriendRequest(CreateFriendRequest friendRequest, String token);
}
