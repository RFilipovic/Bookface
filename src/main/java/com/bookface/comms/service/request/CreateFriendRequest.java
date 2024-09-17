package com.bookface.comms.service.request;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateFriendRequest {
    private Long recipientId;
}
