package com.maojie.evouchersystem.evouchermanagementsystem.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.UserType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtProvider {

    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public static String generateCustomerToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        Date currentDate = new Date();

        String jwt = Jwts.builder()
                        .issuedAt(currentDate)
                        .expiration(new Date(currentDate.getTime()+(1000*60*60*24)))
                        .claim("loginType", UserType.CUSTOMER.toString())
                        .claim("mobileNoString", auth.getName())
                        .claim("authorities", roles)
                        .signWith(key)
                        .compact();

        return jwt;

    }

    public static String generateOwnerToken(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        Date currentDate = new Date();

        String jwt = Jwts.builder()
                        .issuedAt(currentDate)
                        .expiration(new Date(currentDate.getTime()+(1000*60*60*24)))
                        .claim("loginType", UserType.OWNER.toString())
                        .claim("userName", auth.getName())
                        .claim("authorities", roles)
                        .signWith(key)
                        .compact();

        return jwt;

    }

    public static String getUserNameFromToken(String token) throws Exception {

        if(token != null && token.length() >=  JwtConstant.JWT_PREFIX.length()) {
            token = token.substring(JwtConstant.JWT_PREFIX.length());
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

            String userName = String.valueOf(claims.get("userName"));
            return userName;
        }

        throw new Exception("Unable to retrieve user name from token");
        
    }

    public static String getMobielNoStringFromToken(String token) throws Exception {

        if(token != null && token.length() >=  JwtConstant.JWT_PREFIX.length()) {
            token = token.substring(JwtConstant.JWT_PREFIX.length());
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();

            String mobileString = String.valueOf(claims.get("mobileNoString"));
            return mobileString;
        }

        throw new Exception("Unable to retrieve mobile number from token");
        
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> auth = new HashSet<>();
        for(GrantedAuthority ga : authorities) {
            auth.add(ga.getAuthority());
        }

        return String.join(",", auth);

    }

}
