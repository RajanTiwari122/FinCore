package com.bank.ib.security;

import com.bank.ib.auth.dto.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class JwtAccessDeniedHandler
        implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException ex)
            throws IOException {

        ApiErrorResponse error = new ApiErrorResponse();
        error.setStatus(HttpServletResponse.SC_FORBIDDEN);
        error.setError("FORBIDDEN");
        error.setMessage("You do not have permission to access this resource");
        error.setPath(request.getRequestURI());
        error.setTimestamp(Instant.now());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}

