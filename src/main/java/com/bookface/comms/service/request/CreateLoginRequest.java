package com.bookface.comms.service.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateLoginRequest {
    private String username;
    private String password;
}
