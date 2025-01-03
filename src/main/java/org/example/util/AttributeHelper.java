package org.example.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.example.entity.MatchScore;
import org.example.entity.Points;

@Log4j2
@UtilityClass
public class AttributeHelper {

    private static final String TROPHY_ICON = "\uD83C\uDFC6";

    public static void setMatchScoreAttributes(HttpServletRequest request, MatchScore matchScore,
                                               String winnerName, String looserName,
                                               String winnerPrefix, String looserPrefix) {
        if (!matchScore.isTiebreak()) {
            log.info("Not a tiebreak situation. Getting points");
            Points winnerPoints = getPoints(matchScore, winnerName);
            setPointsAsRequestAttribute(request, winnerPoints, winnerPrefix + "Points");

            Points looserPoints = getPoints(matchScore, looserName);
            setPointsAsRequestAttribute(request, looserPoints, looserPrefix + "Points");

        } else {
            log.info("Tiebreak situation. Getting tiebreak points.");
            int winnerTiebreakPoints = getTiebreakPoints(matchScore, winnerName);
            request.setAttribute(winnerPrefix + "Points", winnerTiebreakPoints);

            int looserTiebreakPoints = getTiebreakPoints(matchScore, looserName);
            request.setAttribute(looserPrefix + "Points", looserTiebreakPoints);
        }

        //передаем games в jsp
        int winnerGames = getGames(matchScore, winnerName);
        request.setAttribute(winnerPrefix + "Games", winnerGames);

        int looserGames = getGames(matchScore, looserName);
        request.setAttribute(looserPrefix + "Games", looserGames);

        //передаем Sets в jsp
        int winnerSets = getSets(matchScore, winnerName);
        request.setAttribute(winnerPrefix + "Sets", winnerSets);

        int looserSets = getSets(matchScore, looserName);
        request.setAttribute(looserPrefix + "Sets", looserSets);
    }

    public static void disableScoreButtons(HttpServletRequest request) {
        request.setAttribute("disableScoreBtnPlayerOne", true);
        request.setAttribute("disableScoreBtnPlayerTwo", true);
    }

    public static void renderFinishedMatchScore(HttpSession session, HttpServletRequest request, MatchScore matchScore) {
        if (session.getAttribute("playerOneName").equals(matchScore.getWinner().getName())) {
            request.setAttribute("playerOneGames", "WINNER!");
            request.setAttribute("playerOnePoints", TROPHY_ICON);

        } else {
            request.setAttribute("playerTwoGames", "WINNER!");
            request.setAttribute("playerTwoPoints", TROPHY_ICON);
        }
    }

    private static void setPointsAsRequestAttribute(HttpServletRequest request, Points points, String attributeName) {
        if (points.getValue() != null) {
            request.setAttribute(attributeName, points.getValue());
        } else {
            request.setAttribute(attributeName, points);
        }
    }

    private static int getSets(MatchScore updatedMatchScore, String playerName) {
        return updatedMatchScore.getPlayerScoreByPlayerName(playerName).getSets();
    }

    private static int getGames(MatchScore updatedMatchScore, String playerName) {
        return updatedMatchScore.getPlayerScoreByPlayerName(playerName).getGames();
    }

    private static int getTiebreakPoints(MatchScore updatedMatchScore, String playerName) {
        return updatedMatchScore.getPlayerScoreByPlayerName(playerName).getTiebreakPoints();
    }

    private static Points getPoints(MatchScore updatedMatchScore, String playerName) {
        return updatedMatchScore.getPlayerScoreByPlayerName(playerName).getPoints();
    }
}
