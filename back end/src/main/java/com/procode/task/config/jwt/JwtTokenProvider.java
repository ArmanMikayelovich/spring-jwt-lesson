package com.procode.task.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.procode.task.exceptions.AuthenticationException;
import com.procode.task.exceptions.ServerSideException;
import com.procode.task.model.dto.UserDto;
import com.procode.task.model.entity.UserEntity;
import com.procode.task.service.Mapper;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenProvider {

    private static final String ROLE_PREFIX = "ROLE_";
    private final ObjectMapper objectMapper;
    @Value("${jwt.token.secret}")
    private String secret;
    private Mapper mapper;

    private Long validityInMilliseconds = 999999999L;

    public JwtTokenProvider(ObjectMapper objectMapper, Mapper mapper) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(UserEntity userEntity) {
        UserDto authenticatedUserDto = mapper.map(userEntity, new UserDto());
        String subject;
        try {
            subject = objectMapper.writeValueAsString(authenticatedUserDto);
        } catch (JsonProcessingException e) {
            throw new ServerSideException("Internal Server error");
        }

        Claims claims = Jwts.claims().setSubject(subject);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        boolean validateToken = validateToken(token);
        if (validateToken) {
            final UserDto userDto = getSubject(token);
            final Set<GrantedAuthority> authorities =
                    Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + userDto.getRole()));

            return new PreAuthenticatedAuthenticationToken(userDto, "", authorities);
        } else throw new AuthenticationException("");
    }

    public UserDto getSubject(String token) {
        final String jsonUserDto = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        try {
            return objectMapper.readValue(jsonUserDto, UserDto.class);
        } catch (JsonProcessingException e) {
            throw new ServerSideException("Internal Server error");
        }

    }

    public Long getUserId(String token) {
        String id = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getId();
        return Long.parseLong(id);
    }

    public Optional<String> resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationException("JWT token is expired or invalid");
        }
    }


}
