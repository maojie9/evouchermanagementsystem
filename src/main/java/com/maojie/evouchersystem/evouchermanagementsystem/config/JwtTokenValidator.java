package com.maojie.evouchersystem.evouchermanagementsystem.config;

import java.io.IOException;
//import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.UserType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if(jwt != null && jwt.length() >=  JwtConstant.JWT_PREFIX.length()) {
            jwt = jwt.substring(JwtConstant.JWT_PREFIX.length());

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                
                Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
/* 
                // Make sure the token does not expires
                if(claims.getExpiration().before(new Date())){
                    throw new Exception();
                }
*/
                UserType loginType = UserType.valueOf(String.valueOf(claims.get("loginType")));
                String authorities = String.valueOf(claims.get("authorities"));
                List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList( authorities);
                String userName; // This username is for login to owner, for login as customer, mobile no will be use as userName

                switch (loginType) {
                    case OWNER:
                    userName = String.valueOf(claims.get("userName"));
                    break;
                    case CUSTOMER:
                    userName = String.valueOf(claims.get("mobileNoString"));
                    break;
                    default:
                        throw new Exception();
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(userName, null, authorityList);
                SecurityContextHolder.getContext().setAuthentication(auth);



            } catch (Exception e) {
                throw new RuntimeException("Invalid Token...");
            }
        }

        filterChain.doFilter(request, response);
    }

}
