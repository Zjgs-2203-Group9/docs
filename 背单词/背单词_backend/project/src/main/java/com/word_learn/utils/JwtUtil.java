package com.word_learn.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.lang.reflect.Proxy;

public class JwtUtil {
    public static JWTVerifier verifier = JWT.require(Algorithm.HMAC256("user")).build();
    public static DecodedJWT getDecodedJWT(String token) {
        return verifier.verify(token);
    }
}
