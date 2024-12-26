package org.example.service;

import org.example.entity.PlayerScore;
import org.example.entity.Points;

public class MatchScoreCalcService {

    private PlayerScore winnerScore;
    private PlayerScore looserScore;

    public void calculateScore(PlayerScore winnerScore, PlayerScore looserScore) {
        this.winnerScore = winnerScore;
        this.looserScore = looserScore;

        calculatePoints(winnerScore.getPoints(), looserScore.getPoints());
    }

    /**
     * logic:
     * if: winner points 0; 15; 30 -> winnerPoints.next() - это условие стоит последним!
     * if: winner points 40, looser points 0; 15; 30 -> winner games+1 both points -> 0
     * if: winner points 30, looser points 40 -> both set to DEUCE
     * if: winner points DEUCE + looser points DEUCE -> winner points ADVANTAGE
     * if: winner points DEUCE + looser points ADVANTAGE -> both to DEUCE
     * if: winner points ADVANTAGE + looser points DEUCE -> winner games+1 both points = 0
     *
     */
    private void calculatePoints(Points winnerPoints, Points looserPoints) {

         if (winnerPoints == Points.FORTY && looserPoints.ordinal() <= Points.THIRTY.ordinal()) {
//            int winnerGames = winnerScore.getGames();
//            // TODO: 26/12/2024 тут подумать что делать с геймами когда 6:6
//            winnerScore.setGames(winnerGames + 1);
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
//            int winnerGames = winnerScore.getGames();
//            // TODO: 26/12/2024 тут подумать что делать с геймами когда 6:6
//            winnerScore.setGames(winnerGames + 1);
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
     *
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
            // TODO: 26/12/2024 write tiebreak logic
        }
    }

}
