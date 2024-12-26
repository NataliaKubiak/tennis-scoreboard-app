package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.example.entity.Player;
import org.example.service.OngoingMatchesService;
import org.example.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Log4j2
@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Processing GET request for Match Score page.");

        String queryParam = request.getQueryString();
        log.info("Received query parameters: {}", queryParam);

        if (!Validator.isValidUuidQueryParam(queryParam)) {
            log.error("Invalid query parameters: {}", queryParam);

            // TODO: 26/12/2024 тут наверное надо поставить exception и forward на error page в exception handling filter
            request.getRequestDispatcher("/error-page.html").forward(request, response);
            log.info("Rendered 'error-page.html'");
            return;
        }

        UUID id = UUID.fromString(queryParam.split("=")[1]);
        List<String> playersNamesByMatchId = ongoingMatchesService.getPlayersNamesByMatchId(id);
        request.setAttribute("playerOneName", playersNamesByMatchId.get(0));
        request.setAttribute("playerTwoName", playersNamesByMatchId.get(1));

        request.getRequestDispatcher("WEB-INF/match-score.jsp").forward(request, response);
        log.info("Forwarded to 'match-score.jsp'");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
