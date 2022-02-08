package com.assignment.helpers.utils;

import com.assignment.configurations.CustomConfigs;
import com.assignment.dto.users.CustomUserDetails;
import com.assignment.helpers.APIError;
import com.assignment.helpers.DefaultConstants;
import com.assignment.helpers.ErrorConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtils {

    @Autowired
    private CustomConfigs customConfigs;

    private final String secret = DefaultConstants.API_SECRET;

    public String extractUsername(String token, HttpServletRequest httpServletRequest,
                                  HttpServletResponse httpServletResponse) {
        return extractClaim(token, Claims::getSubject, httpServletRequest, httpServletResponse);
    }

    public Date extractExpiration(
            String token,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return extractClaim(token, Claims::getExpiration,httpServletRequest,httpServletResponse);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        final Claims claims = extractAllClaims(token,httpServletRequest,httpServletResponse);
        return claims != null? claimsResolver.apply(claims): null;
    }
    @SneakyThrows
    private Claims extractAllClaims(
            String token,HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        Claims claims = null;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e) {
            raiseException(httpServletRequest,httpServletResponse);
        }catch (SignatureException e) {
            raiseSignatureException(httpServletRequest,httpServletResponse, e);
        }catch (MalformedJwtException e) {
            raiseMalformedJwtException(httpServletRequest,httpServletResponse, e);
        }
        return claims;
    }

    private Boolean isTokenExpired(
            String token,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws ExpiredJwtException {
        return extractExpiration(token,httpServletRequest,httpServletResponse).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * customConfigs.getJwtExpiryTime()))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateToken(
            String token, UserDetails userDetails,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        CustomUserDetails user = (CustomUserDetails) userDetails;
        final String username = extractUsername(token, httpServletRequest,httpServletResponse);
        return ((username.equals(userDetails.getUsername()))
                && !isTokenExpired(token,httpServletRequest,httpServletResponse));
    }

    public static void raiseException(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        buildServletFilterResponseBody(response, ErrorConstants.UNAUTHORIZED, "Expired token provided");
    }

    public static void raiseSignatureException(
            HttpServletRequest request,
            HttpServletResponse response,
            SignatureException e)
            throws IOException, ServletException {
        buildServletFilterResponseBody(response, ErrorConstants.UNAUTHORIZED, e.getMessage());
    }

    public static void raiseMalformedJwtException(
            HttpServletRequest request,
            HttpServletResponse response,
            MalformedJwtException e)
            throws IOException, ServletException {
        buildServletFilterResponseBody(response, ErrorConstants.UNAUTHORIZED, ErrorConstants.MALFORMED_JWT_SIGNATURE);
    }

    public static void raiseGenericException(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception e)
            throws IOException, ServletException {
        buildServletFilterResponseBody(response, ErrorConstants.UNAUTHORIZED, e.getMessage());
    }


    public static void raiseMissingTokenException(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {
        buildServletFilterResponseBody(response, ErrorConstants.UNAUTHORIZED, ErrorConstants.MISSING_AUTHORIZATION_HEADER);
    }

    private static void buildServletFilterResponseBody(HttpServletResponse response, String unauthorized, String malformedJwtSignature) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        APIError apiError = APIError.builder()
                .message(unauthorized)
                .reason(malformedJwtSignature)
                .build();
        byte[] body = new ObjectMapper().writeValueAsBytes(apiError);
        response.getOutputStream().write(body);
    }
}
