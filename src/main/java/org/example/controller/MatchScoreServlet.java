package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.example.entity.MatchScore;
import org.example.entity.Points;
import org.example.service.MatchScoreCalcService;
import org.example.service.OngoingMatchesService;
import org.example.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();
    private final MatchScoreCalcService matchScoreCalcService = new MatchScoreCalcService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Processing GET request for Match Score page.");
        HttpSession session = request.getSession();

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
        log.info("Parsed match ID: {}", id);
        session.setAttribute("matchId", id);

        List<String> playersNamesByMatchId = ongoingMatchesService.getPlayersNamesByMatchId(id);
        log.info("Got player names: {}, {}", playersNamesByMatchId.get(0), playersNamesByMatchId.get(1));

        session.setAttribute("playerOneName", playersNamesByMatchId.get(0));
        session.setAttribute("playerTwoName", playersNamesByMatchId.get(1));

        request.getRequestDispatcher("WEB-INF/match-score.jsp").forward(request, response);
        log.info("Forwarded to 'match-score.jsp'");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Processing POST request for Match Score page.");

        HttpSession session = request.getSession();
        UUID matchId = (UUID) session.getAttribute("matchId");
        String playerOneName = (String) session.getAttribute("playerOneName");
        String playerTwoName = (String) session.getAttribute("playerTwoName");

        String playerNo = request.getParameter("playerNo");
        log.info("Player number received from request: {}", playerNo);

        MatchScore matchScore = ongoingMatchesService.getMatchScoreById(matchId);

        switch (playerNo) {
            case "1" -> {
                log.info("Processing score for Player 1.");
                processPlayerScore(session, matchScore, playerOneName, playerTwoName, "playerOne", "playerTwo");
            }
            case "2" -> {
                log.info("Processing score for Player 2.");
                processPlayerScore(session, matchScore, playerTwoName, playerOneName, "playerTwo", "playerOne");
            }
            default -> {
                log.warn("Invalid player number received: {}", playerNo);
                return;
            }
        }

        request.getRequestDispatcher("WEB-INF/match-score.jsp").forward(request, response);
        log.info("Forwarded to 'match-score.jsp'");
    }

    private void processPlayerScore(HttpSession session, MatchScore matchScore,
                                    String winnerName, String looserName,
                                    String winnerPrefix, String looserPrefix) {

        log.info("Calculating score for winner={} and looser={}", winnerName, looserName);
        MatchScore updatedMatchScore = matchScoreCalcService.calculateScore(matchScore, winnerName, looserName);

        if (!updatedMatchScore.isTiebreak()) {
            log.info("Not a tiebreak situation. Updating points.");
            Points winnerPoints = getPoints(updatedMatchScore, winnerName);
            setPointsAsSessionAttribute(session, winnerPoints, winnerPrefix + "Points");

            Points looserPoints = getPoints(updatedMatchScore, looserName);
            setPointsAsSessionAttribute(session, looserPoints, looserPrefix + "Points");

        } else {
            log.info("Tiebreak situation. Updating tiebreak points.");
            int winnerTiebreakPoints = getTiebreakPoints(updatedMatchScore, winnerName);
            session.setAttribute(winnerPrefix + "Points", winnerTiebreakPoints);

            int looserTiebreakPoints = getTiebreakPoints(updatedMatchScore, looserName);
            session.setAttribute(looserPrefix + "Points", looserTiebreakPoints);
        }

        int winnerGames = getGames(updatedMatchScore, winnerName);
        session.setAttribute(winnerPrefix + "Games", winnerGames);

        int looserGames = getGames(updatedMatchScore, looserName);
        session.setAttribute(looserPrefix + "Games", looserGames);
    }

    private int getGames(MatchScore updatedMatchScore, String playerOneName) {
        return updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getGames();
    }

    private int getTiebreakPoints(MatchScore updatedMatchScore, String playerOneName) {
        return updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getTiebreakPoints();
    }

    private Points getPoints(MatchScore updatedMatchScore, String playerTwoName) {
        return updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getPoints();
    }

    private void setPointsAsSessionAttribute(HttpSession session, Points points, String attributeName) {
        if (points.getValue() != null) {
            session.setAttribute(attributeName, points.getValue());
        } else {
            session.setAttribute(attributeName, points);
        }
    }

}
