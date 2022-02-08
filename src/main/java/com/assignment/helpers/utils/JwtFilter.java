package com.assignment.helpers.utils;

import com.assignment.entity.users.User;
import com.assignment.helpers.DefaultConstants;
import com.assignment.helpers.UserContext;
import com.assignment.repositories.UserRepository;
import com.assignment.service.users.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger LOG = Logger.getLogger(JwtFilter.class.getName());

    public static final String ANONYMOUS_REQUEST_PATHS = "" +
            "/v1/users/signin:POST" +
            "/v1/users/signup:POST" +
            "/h2-console:GET";


    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private CustomUserDetailsService service;

    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public JwtFilter(UserRepository userRepository, HandlerExceptionResolver handlerExceptionResolver) {
        this.userRepository = userRepository;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest servletRequest) {
        String requestPath = servletRequest.getRequestURI() + ":" + servletRequest.getMethod();
        return ANONYMOUS_REQUEST_PATHS.contains(requestPath);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) {

        try {
            String authorizationHeader = httpServletRequest.getHeader(DefaultConstants.AUTHENTICATION_TOKEN_HEADER_NAME);

            String token;
            String userName;

            if (authorizationHeader != null && authorizationHeader.startsWith(DefaultConstants.BEARER)) {
                token = authorizationHeader.substring(7);
                userName = jwtUtil.extractUsername(token, httpServletRequest, httpServletResponse);
            } else {
                logger.error("Missing Authorization Header");
                JwtUtils.raiseMissingTokenException(httpServletRequest, httpServletResponse);
                return;
            }

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = service.loadUserByUsername(userName);

                if (jwtUtil.validateToken(token, userDetails, httpServletRequest, httpServletResponse)) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource()
                                    .buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    // Set authenticated  user in context
                    User loggedInUser = userRepository.findByUsername(userName).orElseThrow();
                    UserContext.setLoggedInUser(loggedInUser);
                }
            } else {
                return;
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);

        } catch (Exception ex) {
            LOG.log(Level.WARNING, "Request failure : ", ex);
            handlerExceptionResolver.resolveException(httpServletRequest, httpServletResponse, null, ex);
        }
    }
}
