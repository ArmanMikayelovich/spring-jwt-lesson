package com.procode.task.config.jwt;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@Service
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        Optional<String> optionalJwtToken = jwtTokenProvider
                .resolveToken(servletRequest);
        optionalJwtToken.map(jwtTokenProvider::getAuthentication)
                .ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
