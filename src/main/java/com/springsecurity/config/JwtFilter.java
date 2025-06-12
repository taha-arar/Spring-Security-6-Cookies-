package com.springsecurity.config;

import com.springsecurity.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException
    {
        String authorizationHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
             jwtToken = authorizationHeader.substring(7);
             username = jwtService.extractUsername(jwtToken);

        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if(jwtService.validateToken(jwtToken, username)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, jwtService.getAuthorities(jwtToken));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken); // Add the token to the chain (when send it in postman it works with that token)
            }
        }
        filterChain.doFilter(request, response);
    }


}
