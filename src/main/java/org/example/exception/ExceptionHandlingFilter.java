package org.example.exception;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try {
            filterChain.doFilter(servletRequest, servletResponse);

        } catch (InvalidQueryParameterException e) {
            ExceptionHandler.handleBadRequest((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

        } catch (Exception e) {
            ExceptionHandler.handleUnexpectedException((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        }
    }
}
