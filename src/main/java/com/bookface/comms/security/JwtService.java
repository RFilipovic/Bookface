package com.bookface.comms.security;
import com.bookface.comms.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(User user){

        Date now = new Date();
        Date expiriyDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("user_id", user.getUserId())
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiriyDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractUserId(String token){
        Integer userIdInteger = extractClaim(token, claims -> claims.get("user_id", Integer.class));
        Long userIdLong = userIdInteger.longValue();
        System.out.println("Extracted User ID (Integer): " + userIdInteger);
        System.out.println("Extracted User ID (Long): " + userIdLong);
        return userIdLong;
    }

    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token, UserDetails user){
        final String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
}
