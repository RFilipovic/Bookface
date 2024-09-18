package com.bookface.comms.service.request;

import com.bookface.comms.domain.FriendRequestAction;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateFriendRequestUpdate {
    private FriendRequestAction action;
    private Long senderId;
}
