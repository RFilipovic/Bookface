package com.bookface.comms.service.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
}
