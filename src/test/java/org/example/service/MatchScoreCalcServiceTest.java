package org.example.service;

import org.example.entity.MatchScore;
import org.example.entity.Player;
import org.example.entity.PlayerScore;
import org.example.entity.Points;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MatchScoreCalcServiceTest {

    private final MatchScoreCalcService matchScoreCalcService = new MatchScoreCalcService();

    //same for 30, 40
    @Test
    void testCalcScore_WinnerGet15() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won (for both players):
        //points = Points.LOVE; games = 0; sets = 0; tiebreakPoints = 0;
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(15, winnerScoreMock.getPoints().getValue(), "Winner Player should have 15");
        assertEquals(0, looserScoreMock.getPoints().getValue(), "Looser Player should have 0");
    }

    @Test
    void testCalcScore_PlayersGetDEUCE() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: points = 30; looser: points = 40
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setPoints(Points.THIRTY);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setPoints(Points.FORTY);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(Points.DEUCE, winnerScoreMock.getPoints(), "Winner Player should have DEUCE");
        assertEquals(Points.DEUCE, looserScoreMock.getPoints(), "Looser Player should have DEUCE");

    }

}