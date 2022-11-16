package com.auth.authdecoder.filter;

import com.auth.authdecoder.dto.ErrorResponse;
import com.auth.authdecoder.exception.InvalidJwtException;
import com.auth.authdecoder.provider.JwtTokenProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        try {
            String token = jwtTokenProvider.extractToken((HttpServletRequest) request);

            if (Objects.nonNull(token)) {
                jwtTokenProvider.validateToken(token).ifPresent(body -> {
                    Authentication authentication = jwtTokenProvider.getAuthentication(body, token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            }

            filterChain.doFilter(request, response);
        } catch (Throwable e) {

                ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
                if (e instanceof InvalidJwtException) {
                    logger.warn("Could not authorize the token", e);
                    ((HttpServletResponse) response).setStatus(HttpStatus.UNAUTHORIZED.value());
                } else {
                    logger.error("Could not authorize the token", e);
                    ((HttpServletResponse) response).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                }
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write(convertObjectToJson(errorResponse));
            }
    }

    private String convertObjectToJson(Object object) throws IOException {
        if (Objects.isNull(object)) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
