package com.issueflow.security;

import com.issueflow.modal.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final static String SECRET_KEY = "ajfjakfjurqr5827525828582jajfaurqwutqu582835ajfffffhfjafjafahfafjafNVnfjajfja";


    public String extractEmail(String token){
        return extractClaims(token, Claims::getSubject);
    }

    public boolean validateToken(String token, String email){
        return extractEmail(token).equals(email) && !isTokenExpired(token);
    }

    public String generateToken(User user){
        Map<String, Object> roles = new HashMap<>();
        roles.putIfAbsent("role", user.getUserRole().name());

        return Jwts.builder()
                .subject(user.getEmail())
                .claims(roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ 24*60*60*1000))
                .signWith(getSecretKey())
                .compact();
    }

    private boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }

    private SecretKey getSecretKey(){
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private <T> T extractClaims(String token, Function<Claims, T> resolver){
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


}
