package org.example.service;

import org.example.entity.MatchScore;
import org.example.entity.PlayerScore;
import org.example.entity.Points;

public class MatchScoreCalcService {

    private MatchScore matchScore;

    private PlayerScore winnerScore;
    private PlayerScore looserScore;

    public MatchScore calculateScore(MatchScore matchScore, String winnerName, String looserName) {
        this. matchScore = matchScore;
        winnerScore = matchScore.getPlayerScoreByPlayerName(winnerName);
        looserScore = matchScore.getPlayerScoreByPlayerName(looserName);

        if (!matchScore.isTiebreak()) {
            calculatePoints(winnerScore.getPoints(), looserScore.getPoints());
        } else {
            calculateTiebreak(winnerScore.getTiebreakPoints(), looserScore.getTiebreakPoints());
        }

        return matchScore;
    }

    /**
     * logic:
     * if: winner points 0; 15; 30 -> winnerPoints.next() - это условие стоит последним!
     * if: winner points 40, looser points 0; 15; 30 -> winner games+1 both points -> 0
     * if: winner points 30, looser points 40 -> both set to DEUCE
     * if: winner points DEUCE + looser points DEUCE -> winner points ADVANTAGE
     * if: winner points DEUCE + looser points ADVANTAGE -> both to DEUCE
     * if: winner points ADVANTAGE + looser points DEUCE -> winner games+1 both points = 0
     */
    private void calculatePoints(Points winnerPoints, Points looserPoints) {

        if (winnerPoints == Points.FORTY && looserPoints.ordinal() <= Points.THIRTY.ordinal()) {
            calculateGames(winnerScore.getGames(), looserScore.getGames());
            winnerScore.setPoints(Points.LOVE);
            looserScore.setPoints(Points.LOVE);

        } else if (winnerPoints == Points.THIRTY && looserPoints == Points.FORTY) {
            winnerScore.setPoints(Points.DEUCE);
            looserScore.setPoints(Points.DEUCE);

        } else if (winnerPoints == Points.DEUCE && looserPoints == Points.DEUCE) {
            winnerScore.setPoints(Points.ADVANTAGE);

        } else if (winnerPoints == Points.DEUCE && looserPoints == Points.ADVANTAGE) {
            winnerScore.setPoints(Points.DEUCE);
            looserScore.setPoints(Points.DEUCE);

        } else if (winnerPoints == Points.ADVANTAGE && looserPoints == Points.DEUCE) {
            calculateGames(winnerScore.getGames(), looserScore.getGames());
            winnerScore.setPoints(Points.LOVE);
            looserScore.setPoints(Points.LOVE);

        } else if (winnerPoints == Points.LOVE
                || winnerPoints == Points.FIFTEEN
                || winnerPoints == Points.THIRTY) {
            winnerScore.setPoints(winnerPoints.next());
        }
    }

    /**
     * logic:
     * winner games 1, 2, 3, 4, 5; looser games < 6 (1, 2, 3, 4, 5) -> winner games +1
     * winner games = 6; looser games < 6 (1, 2, 3, 4, 5) -> both games = 0, winner sets+1
     * winner games = 5; looser games = 6 -> tiebreak starts
     */
    private void calculateGames(int winnerGames, int looserGames) {

        if (winnerGames <= 5 && looserGames <= 5) {
            winnerScore.setGames(winnerGames + 1);

        } else if (winnerGames == 6 && looserGames <= 5) {
            int winnerSets = winnerScore.getSets();
            winnerScore.setSets(winnerSets + 1);
            winnerScore.setGames(0);
            looserScore.setGames(0);

        } else if (winnerGames == 5 && looserGames == 6) {
            winnerScore.setGames(winnerGames + 1);
            matchScore.setTiebreak(true);
        }
    }

    /**
     * logic:
     * winner points <=5; looser points <=6 -> winner points +1
     * winner points = 6; looser points = 5 -> winner sets +1, all games = 0, tiebreakPoints -> points
     * winner points >= 7; looser points = winnerPoints-1 -> winner sets +1, all games = 0, tiebreakPoints -> points
     * winner points >= 7; looser points = winnerPoints -> winner points +1
     *
     */
    private void calculateTiebreak(int winnerTiebreakPoints, int looserTiebreakPoints) {

        if (winnerTiebreakPoints == 6 && looserTiebreakPoints <= 5) {
            finishTiebreak();

        } else if (winnerTiebreakPoints <= 6 && looserTiebreakPoints <=6) {
             winnerScore.setTiebreakPoints(winnerTiebreakPoints + 1);

        } else if (winnerTiebreakPoints >= 7 && looserTiebreakPoints == (winnerTiebreakPoints - 1)) {
            finishTiebreak();

        } else if (winnerTiebreakPoints >= 7 && looserTiebreakPoints == winnerTiebreakPoints) {
            winnerScore.setTiebreakPoints(winnerTiebreakPoints + 1);
        }
    }

    private void finishTiebreak() {
        int winnerSets = winnerScore.getSets();
        winnerScore.setSets(winnerSets + 1);

        winnerScore.setGames(0);
        looserScore.setGames(0);
        winnerScore.setTiebreakPoints(0);
        looserScore.setTiebreakPoints(0);

        matchScore.setTiebreak(false);
    }
}
