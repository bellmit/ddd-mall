package com.tactbug.mall.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tactbug.mall.common.base.JwtInfo;

import java.util.Date;
import java.util.UUID;

public class JwtUtil {

    private static final String ISSUER = "Tact_Bug";

    public static String generateJwt(Long userId, JwtInfo jwtInfo){
        Date currentTime = new Date();
        Algorithm ALG = Algorithm.HMAC256(jwtInfo.getSign());

        return JWT.create()
                .withIssuer(ISSUER) // 发行者
                .withSubject(jwtInfo.getSubject()) // 用户身份标识
                .withIssuedAt(currentTime) // 签发时间
                .withExpiresAt(new Date(currentTime.getTime() + jwtInfo.getValidMillis())) // 有效期
                .withClaim("userId", userId)
                .withJWTId(UUID.randomUUID().toString())
                .sign(ALG);
    }

    public static DecodedJWT verifyToken(String token, JwtInfo jwtInfo){
        Algorithm ALG = Algorithm.HMAC256(jwtInfo.getSign());
        JWTVerifier jwtVerifier = JWT.require(ALG)
                .withSubject(jwtInfo.getSubject())
                .build();
        return jwtVerifier.verify(token);
    }
}
