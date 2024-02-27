package com.bookface.comms;
import com.bookface.comms.domain.User;
import com.bookface.comms.security.JwtService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;


public class JwtServiceTest {

    @Test
    public void testGenerateToken() {
        JwtService jwtService = new JwtService();
        jwtService.setExpiration(3600L);
        jwtService.setSecret("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        User user = new User();
        user.setName("testUser");
        user.setUserId(123L);
        user.setEmail("test@example.com");
        String token = jwtService.generateToken(user);
        Assertions.assertNotNull(token);
    }

    @Test
    public void testExtractUsername() {
        JwtService jwtService = new JwtService();
        jwtService.setExpiration(3600L);
        jwtService.setSecret("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmaWxpcCIsInVzZXJfaWQiOjQsImVtYWlsIjoiZmlsaXAuZ2F5QGZlci5ociIsImlhdCI6MTcwOTA1NDY2OCwiZXhwIjoxNzA5MDU4MjY4fQ.BeDVctyK2AEyIO9WUdiVx5JvX98EzEW72baLFNH-g6s"; // Replace with a valid JWT token
        String username = jwtService.extractUsername(token);
        Assertions.assertNotNull(username);
    }

    @Test
    public void testExtractUserId() {

        User user = new User();
        user.setName("testUser");
        user.setUserId(123L);
        user.setEmail("test@example.com");

        JwtService jwtService = new JwtService();
        jwtService.setExpiration(3600L);
        jwtService.setSecret("404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");

        String token = jwtService.generateToken(user);
        Long extractedUserId = jwtService.extractUserId(token);
        Assertions.assertEquals(user.getUserId(), extractedUserId);
    }

}
