package com.condoconnect.condo.service;

import com.condoconnect.condo.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;
import java.util.Date;

public class TokenUtil {
    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION = 12 * 60 * 60 * 1000;
    private static final String EMISSOR = "CondoConnect";
    private static final KeyPair keyPair = generateKeyPair();

    private static KeyPair generateKeyPair() {
        try {
            KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
            return keyPair;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate key pair", e);
        }
    }

    public static String createToken(User user) {
        PrivateKey privateKey = keyPair.getPrivate();
        String token = Jwts.builder()
                .setSubject(user.getName())
                .setIssuer(EMISSOR)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();

        return PREFIX + token;
    }

    private static boolean isExpirationValid(Date expiration) {
        return expiration.after(new Date(System.currentTimeMillis()));
    }

    private static boolean isIssuerValid(String issuer) {
        return issuer.equals(EMISSOR);
    }

    private static boolean isSubjectValid(String username) {
        return username != null && username.length() > 0;
    }

    public static Authentication validate(HttpServletRequest request) {
        String token = request.getHeader(HEADER);
        if (token == null || !token.startsWith(PREFIX)) {
            return null;
        }
        token = token.replace(PREFIX, "");

        try {
            PublicKey publicKey = keyPair.getPublic();
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);

            String username = jwsClaims.getBody().getSubject();
            String issuer = jwsClaims.getBody().getIssuer();
            Date expiration = jwsClaims.getBody().getExpiration();

            if (isSubjectValid(username) && isIssuerValid(issuer) && isExpirationValid(expiration)) {
                return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            }
        } catch (Exception e) {
            // Log the exception and return null if the token is invalid
            e.printStackTrace();
        }
        return null;
    }
}
