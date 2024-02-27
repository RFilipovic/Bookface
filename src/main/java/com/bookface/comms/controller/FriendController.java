package com.bookface.comms.controller;
import com.bookface.comms.service.FriendService;
import com.bookface.comms.service.request.CreateFriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController(FriendService friendService){
        this.friendService = friendService;
    }

    //send friend request
    @PostMapping("/requests")
    public ResponseEntity<String> sendFriendRequest(
            @RequestBody CreateFriendRequest friendRequest,
            @RequestHeader("Authorization") String token
    ){
        friendService.sendFriendRequest(friendRequest, token);
        return ResponseEntity.ok("Friend request has been sent.");
    }

    //accept or decline the request

}
