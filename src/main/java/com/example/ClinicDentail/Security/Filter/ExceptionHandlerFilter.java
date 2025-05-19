package com.example.ClinicDentail.Security.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver resolver;
    private final ObjectMapper objectMapper;

    public ExceptionHandlerFilter(HandlerExceptionResolver resolver, ObjectMapper objectMapper) {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // Thử giải quyết lỗi bằng HandlerExceptionResolver
            if (!response.isCommitted()) {
                if (ex instanceof jakarta.validation.ValidationException || ex.getCause() instanceof jakarta.validation.ValidationException) {
                    handleValidationException(response, ex);
                } else {
                    resolver.resolveException(request, response, null, ex);
                }
            } else {
                logger.error("Response already committed. Unable to handle exception: " + ex.getMessage(), ex);
            }
        }
    }

    private void handleValidationException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("status", HttpStatus.BAD_REQUEST.value());
        errorDetails.put("error", "Validation Error");
        errorDetails.put("message", ex.getMessage());

        objectMapper.writeValue(response.getOutputStream(), errorDetails);
    }
}