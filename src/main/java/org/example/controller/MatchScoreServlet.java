package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.example.dto.MatchDto;
import org.example.entity.MatchScore;
import org.example.entity.Points;
import org.example.exception.InvalidQueryParameterException;
import org.example.service.FinishedMatchesPersistService;
import org.example.service.MatchScoreCalcService;
import org.example.service.OngoingMatchesService;
import org.example.util.AttributeHelper;
import org.example.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final MatchScoreCalcService matchScoreCalcService = new MatchScoreCalcService();
    private final FinishedMatchesPersistService finishedMatchesPersistService = new FinishedMatchesPersistService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Processing GET request for Match Score page.");
        HttpSession session = request.getSession();

        String queryParam = request.getQueryString();
        log.info("Received query parameters: {}", queryParam);

        if (!Validator.isValidUuidQueryParam(queryParam)) {
            log.error("Invalid query parameter: {}", queryParam);
            throw new InvalidQueryParameterException("Invalid query parameter: " + queryParam);
        }

        UUID uuid = UUID.fromString(queryParam.split("=")[1]);
        log.info("Parsed match ID: {}", uuid);
        request.setAttribute("uuid", uuid);

        List<String> playersNamesByMatchId = ongoingMatchesService.getPlayersNamesByMatchId(uuid);
        log.info("Got player names: {}, {}", playersNamesByMatchId.get(0), playersNamesByMatchId.get(1));

        String playerOneName = playersNamesByMatchId.get(0);
        String playerTwoName = playersNamesByMatchId.get(1);

        session.setAttribute("playerOneName", playerOneName);
        session.setAttribute("playerTwoName", playerTwoName);

        MatchScore matchScore = ongoingMatchesService.getMatchScoreById(uuid);

        AttributeHelper.setMatchScoreAttributes(request, matchScore, playerOneName, playerTwoName);

        if (matchScore.isMatchFinished()) {
            finishedMatchesPersistService.saveMatch(matchScore);

            AttributeHelper.renderFinishedMatchScore(session, request, matchScore);
            AttributeHelper.disableScoreButtons(request);

            ongoingMatchesService.removeMatchFromMap(uuid);
        }

        request.getRequestDispatcher("WEB-INF/match-score.jsp").forward(request, response);
        log.info("Forwarded to 'match-score.jsp'");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Processing POST request for Match Score page.");

        HttpSession session = request.getSession();
        String uuidParam = request.getParameter("uuid");
        UUID uuid = UUID.fromString(uuidParam);

        String playerOneName = (String) session.getAttribute("playerOneName");
        String playerTwoName = (String) session.getAttribute("playerTwoName");

        String playerNo = request.getParameter("playerNo");
        log.info("Player number received from request: {}", playerNo);

        MatchScore matchScore = ongoingMatchesService.getMatchScoreById(uuid);

        switch (playerNo) {
            case "1" -> {
                log.info("Player 1 won Point! " +
                        "Calculating score for Winner = Player 1: {}, Looser = Player 2: {}",
                        playerOneName, playerTwoName);
                matchScoreCalcService.calculateScore(matchScore, playerOneName, playerTwoName);
            }
            case "2" -> {
                log.info("Player 2 won Point! " +
                        "Calculating score for Winner = Player 2: {}, Looser = Player 1: {}",
                        playerTwoName, playerOneName);
                matchScoreCalcService.calculateScore(matchScore, playerTwoName, playerOneName);
            }
            default -> {
                log.warn("Invalid player number received: {}", playerNo);
                return;
            }
        }

        response.sendRedirect("/match-score?uuid=" + uuid);
        log.info("Redirected to 'match-score.jsp'");
    }
}
