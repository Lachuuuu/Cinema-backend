package com.Cinema.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final JwtService jwtService;

   private final UserDetailsService userDetailsService;

   @Override
   protected void doFilterInternal(
         @NotNull HttpServletRequest request,
         @NotNull HttpServletResponse response,
         @NotNull FilterChain filterChain
   ) throws ServletException, IOException {
      final Cookie cookie = WebUtils.getCookie(request, "jwt");

      String jwt = null;
      if (cookie != null) jwt = cookie.getValue();

      final String userEmail;
      if (jwt == null) {
         filterChain.doFilter(request, response);
         return;
      }

      userEmail = jwtService.extractUserEmail(jwt);
      if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
         UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
         if (jwtService.isTokenValid(jwt, userDetails)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                  userDetails,
                  null,
                  userDetails.getAuthorities()
            );
            authToken.setDetails(
                  new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
         }
      }
      filterChain.doFilter(request, response);
   }
}
