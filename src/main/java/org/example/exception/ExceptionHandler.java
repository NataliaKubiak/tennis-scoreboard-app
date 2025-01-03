package org.example.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class ExceptionHandler {

    //Status: 400
    public static void handleBadRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        forwardToErrorPage(request, response);
    }

    public static void handleUnexpectedException(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        forwardToErrorPage(request, response);
    }

    private static void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/error-page.html").forward(request, response);
        log.info("Forwarded to 'error-page.html'");
    }
}
