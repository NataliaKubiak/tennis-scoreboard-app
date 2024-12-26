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
import org.example.service.OngoingMatchesService;
import org.example.util.Validator;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Log4j2
@WebServlet("/match-score")
public class MatchScoreServlet extends HttpServlet {

    private final OngoingMatchesService ongoingMatchesService = OngoingMatchesService.getInstance();

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
        session.setAttribute("matchId", id);

        List<String> playersNamesByMatchId = ongoingMatchesService.getPlayersNamesByMatchId(id);
        session.setAttribute("playerOneName", playersNamesByMatchId.get(0));
        session.setAttribute("playerTwoName", playersNamesByMatchId.get(1));

        request.getRequestDispatcher("WEB-INF/match-score.jsp").forward(request, response);
        log.info("Forwarded to 'match-score.jsp'");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UUID matchId = (UUID) session.getAttribute("matchId");
        String  playerOneName = (String) session.getAttribute("playerOneName");
        String  playerTwoName = (String) session.getAttribute("playerTwoName");

        String playerNo = request.getParameter("playerNo");

        switch (playerNo) {
            case "1" -> {
                MatchScore updatedMatchScore = ongoingMatchesService.updateMatchScoreWithId(matchId, playerOneName, playerTwoName);

                Points winnerPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getPoints();
                if (winnerPoints.getValue() != null) {
                    session.setAttribute("playerOnePoints", winnerPoints.getValue());
                } else {
                    session.setAttribute("playerOnePoints", winnerPoints);
                }

                int winnerGames = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getGames();
                session.setAttribute("playerOneGames", winnerGames);

                Points looserPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getPoints();
                if (looserPoints.getValue() != null) {
                    session.setAttribute("playerTwoPoints", looserPoints.getValue());
                } else {
                    session.setAttribute("playerTwoPoints", looserPoints);
                }

                int looserGames = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getGames();
                session.setAttribute("playerTwoGames", looserGames);
            }
            case "2" -> {
                MatchScore updatedMatchScore = ongoingMatchesService.updateMatchScoreWithId(matchId, playerTwoName, playerOneName);

                Points winnerPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getPoints();
                if (winnerPoints.getValue() != null) {
                    session.setAttribute("playerTwoPoints", winnerPoints.getValue());
                } else {
                    session.setAttribute("playerTwoPoints", winnerPoints);
                }

                int winnerGames = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getGames();
                session.setAttribute("playerTwoGames", winnerGames);

                Points looserPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getPoints();
                if (looserPoints.getValue() != null) {
                    session.setAttribute("playerOnePoints", looserPoints.getValue());
                } else {
                    session.setAttribute("playerOnePoints", looserPoints);
                }

                int looserGames = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getGames();
                session.setAttribute("playerOneGames", looserGames);
            }
            default -> {
                return;
            }
        }

        request.getRequestDispatcher("WEB-INF/match-score.jsp").forward(request, response);
    }
}
