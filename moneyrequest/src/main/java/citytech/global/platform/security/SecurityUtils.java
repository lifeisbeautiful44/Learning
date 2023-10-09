package citytech.global.platform.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Named("securityUtils")

public class SecurityUtils {
    @Inject
    public SecurityUtils() {
    }


    public String token(String email, String userType, Long userId) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.putAll(prepareRequestBody(email, userType, userId));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 12);
        return Jwts.builder().setClaims(claims).setExpiration(calendar.getTime()).signWith(getKey()).compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(getKey()).parseClaimsJws(token).getBody();
    }

    private Key getKey() {
        String jwtSignature = "9f1ba31f-86b8-42aa-9449-1eed715b464d";
        return Keys.hmacShaKeyFor(jwtSignature.getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, String> prepareRequestBody(String email, String userType, Long userId) {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("userType", userType);
        requestBody.put("userId", String.valueOf(userId));
        return requestBody;
    }

    public RequestContext parseTokenAndGetContext(String token) {
        try {
            Claims claims = this.parseToken(token);
            RequestContextBuilder contextBuilder = RequestContextBuilder.builder();
            contextBuilder.userType(claims.get("userType", String.class));
            contextBuilder.subject(claims.get("email", String.class));
//            contextBuilder.userID(claims.get("userID", String.class));
            return contextBuilder.build();

        } catch (Exception exception) {
            if (exception instanceof ExpiredJwtException)
                throw new IllegalArgumentException("Security Token is expired");
            throw new IllegalArgumentException("Security Token is invalid");
        }
    }
}

