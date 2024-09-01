package infootball.user.core.config;


import infootball.user.application.UserSecurityService;
import infootball.user.core.utils.JwtUtil;
import infootball.user.infrastructure.outbound.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final UserSecurityService userSecurityService;

    @Autowired
    private final UserRepository userRepository;

    public JWTFilter(JwtUtil jwtUtil, UserSecurityService userSecurityService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userSecurityService = userSecurityService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println("Authorization Header: " + header);

        if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
            System.out.println("No JWT token found in the request header");
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = header.split(" ")[1].trim();
        System.out.println("Extracted JWT: " + jwt);

        if (!this.jwtUtil.isValid(jwt)) {
            System.out.println("Invalid JWT token");
            filterChain.doFilter(request, response);
            return;
        }

        String emailUser = this.jwtUtil.getEmail(jwt);
        System.out.println("Email extracted from JWT: " + emailUser);

        String requestPath = request.getRequestURI();
        String userIdFromPath = requestPath.substring(requestPath.lastIndexOf("/") + 1);
        System.out.println("User ID extracted from request path: " + userIdFromPath);

        long userIdFromPathCasted;
        try {
            userIdFromPathCasted = Long.parseLong(userIdFromPath);
        } catch (NumberFormatException e) {
            System.out.println("Invalid User ID format: " + userIdFromPath);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("User ID invalid");
            return;
        }

        Optional<infootball.user.domain.model.User> userInDb = this.userRepository.findByEmail(emailUser);
        if (userInDb.isEmpty()) {
            System.out.println("User not found in the database with email: " + emailUser);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied or user not found");
            return;
        }

        if (!userInDb.get().getId().equals(userIdFromPathCasted)) {
            System.out.println("User ID in path does not match the user's ID in the database");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access denied or user not found");
            return;
        }

        User user = (User) this.userSecurityService.loadUserByUsername(emailUser);
        System.out.println("User successfully loaded from UserSecurityService: " + user.getUsername());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        System.out.println("Authentication token set in SecurityContext: " + authenticationToken);

        filterChain.doFilter(request, response);
        System.out.println("Request successfully filtered");
    }
}
