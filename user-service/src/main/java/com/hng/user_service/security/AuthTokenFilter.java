package com.hng.user_service.security;
import com.hng.user_service.user.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            String gatewayHeader = request.getHeader("X-Gateway-Verified");

            if ("true".equalsIgnoreCase(gatewayHeader)) {

                String userId = request.getHeader("X-User-Id");
                String email = request.getHeader("X-User-Email");

                if (userId != null && email != null) {
                    var userDetails = userDetailsService.loadUserByUsername(email);
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }

            } else {

                String header = request.getHeader(HttpHeaders.AUTHORIZATION);

                if (header != null && header.startsWith("Bearer ")) {
                    String token = header.substring(7);
                    var claims = jwtUtils.extractClaims(token);
                    String email = claims.getSubject();

                    var userDetails = userDetailsService.loadUserByUsername(email);
                    var authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

        } catch (Exception ex) {

            logger.error("Could not set user authentication.", ex);
        }


        filterChain.doFilter(request, response);
    }
}

//public class AuthTokenFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private CustomUserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (header != null && header.startsWith("Bearer ")) {
//            String token = header.substring(7);
//
//            try {
//                var claims = jwtUtils.extractClaims(token);
//                String email = claims.getSubject();
//
//                var userDetails = userDetailsService.loadUserByUsername(email);
//                var authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        null,
//                        userDetails.getAuthorities()
//                );
//
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            } catch (Exception ignored) {}
//        }
//
//        filterChain.doFilter(request, response);
//    }
//
//}
