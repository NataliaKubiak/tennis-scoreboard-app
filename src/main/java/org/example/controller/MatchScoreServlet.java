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
        String playerOneName = (String) session.getAttribute("playerOneName");
        String playerTwoName = (String) session.getAttribute("playerTwoName");

        String playerNo = request.getParameter("playerNo");

        switch (playerNo) {
            case "1" -> {
                MatchScore matchScore = ongoingMatchesService.getMatchScoreById(matchId);
                MatchScore updatedMatchScore = matchScoreCalcService.calculateScore(matchScore, playerOneName, playerTwoName);

                if (!updatedMatchScore.isTiebreak()) {
                    Points winnerPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getPoints();
                    setPoints(session, winnerPoints, "playerOnePoints");

                    Points looserPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getPoints();
                    setPoints(session, looserPoints, "playerTwoPoints");

                } else {
                    int winnerTiebreakPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getTiebreakPoints();
                    session.setAttribute("playerOnePoints", winnerTiebreakPoints);

                    int looserTiebreakPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getTiebreakPoints();
                    session.setAttribute("playerTwoPoints", looserTiebreakPoints);
                }

                int winnerGames = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getGames();
                session.setAttribute("playerOneGames", winnerGames);

                int looserGames = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getGames();
                session.setAttribute("playerTwoGames", looserGames);

            }
            case "2" -> {
                MatchScore matchScore = ongoingMatchesService.getMatchScoreById(matchId);
                MatchScore updatedMatchScore = matchScoreCalcService.calculateScore(matchScore, playerTwoName, playerOneName);

                if (!updatedMatchScore.isTiebreak()) {
                    Points winnerPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getPoints();
                    setPoints(session, winnerPoints, "playerTwoPoints");

                    Points looserPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getPoints();
                    setPoints(session, looserPoints, "playerOnePoints");

                } else {
                    int winnerTiebreakPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getTiebreakPoints();
                    session.setAttribute("playerTwoPoints", winnerTiebreakPoints);

                    int looserTiebreakPoints = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getTiebreakPoints();
                    session.setAttribute("playerOnePoints", looserTiebreakPoints);
                }

                int winnerGames = updatedMatchScore.getPlayerScoreByPlayerName(playerTwoName).getGames();
                session.setAttribute("playerTwoGames", winnerGames);

                int looserGames = updatedMatchScore.getPlayerScoreByPlayerName(playerOneName).getGames();
                session.setAttribute("playerOneGames", looserGames);

            }
            default -> {
                return;
            }
        }

        request.getRequestDispatcher("WEB-INF/match-score.jsp").forward(request, response);
    }

    private void setPoints(HttpSession session, Points points, String attributeName) {
        if (points.getValue() != null) {
            session.setAttribute(attributeName, points.getValue());
        } else {
            session.setAttribute(attributeName, points);
        }
    }

}
