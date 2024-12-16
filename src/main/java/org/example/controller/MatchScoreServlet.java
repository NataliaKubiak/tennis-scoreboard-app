package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
@WebServlet("/match-score")
// TODO: 16/12/2024 поменять ендпоинт на /match-score?uuid=$match_id
public class MatchScoreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Processing GET request for Match Score page.");

        request.getRequestDispatcher("WEB-INF/templates/match-score.html").forward(request, response);
        log.info("Forwarded to 'match-score.html' template.");
    }
}
