package pro.hexa.backend.main.api.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pro.hexa.backend.main.api.common.exception.BadRequestException;
import pro.hexa.backend.main.api.common.exception.BadRequestType;

@Component
public class Jwt {

    public static String jwtSecretKey;

    public static final int ACCESS_TOKEN_EXPIRE_MINUTE = 10;
    public static final int REFRESH_TOKEN_EXPIRE_DAY = 1;
    public static final int CHANGE_PW_TOKEN_EXPIRE_MINUTE = 30;

    private static final String BEARER_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_TYPE = "TOKEN_TYPE";
    public static final String JWT_USER_ID = "userId";
    public static final String JWT_ISSUED_AT = "issuedAt";

    @Value("${hexa-page-jwt-secret-key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        Jwt.jwtSecretKey = jwtSecretKey;
    }

    public static String generateAccessToken(String userId) {
        Timestamp expiration = Timestamp.valueOf(LocalDateTime.now().plusMinutes(ACCESS_TOKEN_EXPIRE_MINUTE));
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setExpiration(expiration)
            .claim(JWT_TOKEN_TYPE, JwtTokenType.ACCESS_TOKEN.getValue())
            .claim(JWT_USER_ID, userId)
            .claim(JWT_ISSUED_AT, new Date().getTime())
            .signWith(SignatureAlgorithm.HS256, Jwt.jwtSecretKey)
            .compact();
    }

    public static String generateRefreshToken() {
        Timestamp expiration = Timestamp.valueOf(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRE_DAY));
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setExpiration(expiration)
            .claim(JWT_TOKEN_TYPE, JwtTokenType.REFRESH_TOKEN.getValue())
            .claim(JWT_ISSUED_AT, new Date().getTime())
            .signWith(SignatureAlgorithm.HS256, Jwt.jwtSecretKey)
            .compact();
    }

    public static String generateChangePasswordToken(String userId) {
        Timestamp expiration = Timestamp.valueOf(LocalDateTime.now().plusMinutes(CHANGE_PW_TOKEN_EXPIRE_MINUTE));
        return Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .setExpiration(expiration)
            .claim(JWT_TOKEN_TYPE, JwtTokenType.CHANGE_PW_TOKEN.getValue())
            .claim(JWT_USER_ID, userId)
            .claim(JWT_ISSUED_AT, new Date().getTime())
            .signWith(SignatureAlgorithm.HS256, Jwt.jwtSecretKey)
            .compact();
    }

    public static Claims validate(String token, String signingKey){
        try{
            return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replaceFirst(BEARER_PREFIX, ""))
                .getBody();
        }catch(ExpiredJwtException e){
            throw new BadRequestException(BadRequestType.EXPIRED_TOKEN);
        }catch (JwtException e){
            throw new BadRequestException(BadRequestType.INVALID_TOKEN);
        }
    }
}
