package com.bookface.comms.security;
import com.bookface.comms.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(User user){

        Date now = new Date();
        Date expiriyDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.getName())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiriyDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.ES512)
                .compact();
    }

    


}
