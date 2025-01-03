package org.example.service;

import org.example.entity.MatchScore;
import org.example.entity.Player;
import org.example.entity.PlayerScore;
import org.example.entity.Points;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        //points = Points.LOVE (0); games = 0; sets = 0; tiebreakPoints = 0;
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(15, winnerScoreMock.getPoints().getValue(), "Winner Player should have Points = 15");
        assertEquals(0, looserScoreMock.getPoints().getValue(), "Looser Player should have Points = 0");
    }

    @Test
    void testCalcScore_WinnerGetGame_After_40_15() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: points = 40; looser: points = 15
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setPoints(Points.FORTY);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setPoints(Points.FIFTEEN);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(1, winnerScoreMock.getGames(), "Winner Player should have Game = 1");
        assertEquals(Points.LOVE, winnerScoreMock.getPoints(), "Winner Player should have Points = LOVE");

        assertEquals(Points.LOVE, looserScoreMock.getPoints(), "Looser Player should have Points = LOVE");
    }

    @Test
    void testCalcScore_PlayersGetDEUCE_After_30_40() {
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

    @Test
    void testCalcScore_WinnerGetADVANTAGE() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: points = DEUCE; looser: points = DEUCE
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setPoints(Points.DEUCE);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setPoints(Points.DEUCE);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(Points.ADVANTAGE, winnerScoreMock.getPoints(), "Winner Player should have ADVANTAGE");
        assertEquals(Points.DEUCE, looserScoreMock.getPoints(), "Looser Player should have DEUCE");
    }

    @Test
    void testCalcScore_WinnerGetDEUCE_After_Deuce_Advantage() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: points = DEUCE; looser: points = ADVANTAGE
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setPoints(Points.DEUCE);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setPoints(Points.ADVANTAGE);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(Points.DEUCE, winnerScoreMock.getPoints(), "Winner Player should have DEUCE");
        assertEquals(Points.DEUCE, looserScoreMock.getPoints(), "Looser Player should have DEUCE");
    }

    @Test
    void testCalcScore_WinnerGetGame_After_Advantage_Deuce() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: points = ADVANTAGE; looser: points = DEUCE
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setPoints(Points.ADVANTAGE);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setPoints(Points.DEUCE);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(1, winnerScoreMock.getGames(), "Winner Player should have Game = 1");
        assertEquals(Points.LOVE, winnerScoreMock.getPoints(), "Winner Player should have Points = LOVE");

        assertEquals(Points.LOVE, looserScoreMock.getPoints(), "Looser Player should have Points = LOVE");
    }

    @Test
    void testCalcScore_WinnerGetGame() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: points = 40, games = 4;
        //looser: points = 15, games = 2
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setPoints(Points.FORTY);
        winnerScoreMock.setGames(4);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setPoints(Points.FIFTEEN);
        looserScoreMock.setGames(2);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(5, winnerScoreMock.getGames(), "Winner Player should have Games = 5");
        assertEquals(Points.LOVE, winnerScoreMock.getPoints(), "Winner Player should have Points = LOVE");

        assertEquals(2, looserScoreMock.getGames(), "Looser Player should have Games = 2");
        assertEquals(Points.LOVE, looserScoreMock.getPoints(), "Looser Player should have Points = LOVE");
    }

    @Test
    void testCalcScore_WinnerGetSet() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: points = 40, games = 6, sets = 0;
        //looser: points = 15, games = 2, sets = 0
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setPoints(Points.FORTY);
        winnerScoreMock.setGames(6);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setPoints(Points.FIFTEEN);
        looserScoreMock.setGames(2);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(false);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(1, winnerScoreMock.getSets(), "Winner Player should have Sets = 1");
        assertEquals(0, winnerScoreMock.getGames(), "Winner Player should have Games = 0");
        assertEquals(Points.LOVE, winnerScoreMock.getPoints(), "Winner Player should have Points = LOVE");

        assertEquals(0, looserScoreMock.getSets(), "Looser Player should have Sets = 0");
        assertEquals(0, looserScoreMock.getGames(), "Looser Player should have Games = 0");
        assertEquals(Points.LOVE, looserScoreMock.getPoints(), "Looser Player should have Points = LOVE");
    }

    @Test
    void testCalcScore_TiebreakStarts() {
        // Arrange
        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //тут используем реальный объект, а не заглушку потому что isTiebreak относится к matchScore
        MatchScore matchScore = new MatchScore(playerWinner, playerLooser);

        // Initial points before winner won:
        // winner: points = 40, games = 5, sets = 0;
        // looser: points = 15, games = 6, sets = 0
        PlayerScore winnerScore = matchScore.getPlayerScoreByPlayerName("Player1");
        winnerScore.setPoints(Points.FORTY);
        winnerScore.setGames(5);

        PlayerScore looserScore = matchScore.getPlayerScoreByPlayerName("Player2");
        looserScore.setPoints(Points.FIFTEEN);
        looserScore.setGames(6);

        // Act
        matchScoreCalcService.calculateScore(matchScore, "Player1", "Player2");

        // Assert
        assertEquals(6, winnerScore.getGames(), "Winner Player should have Games = 6");
        assertEquals(0, winnerScore.getPoints().getValue(), "Winner Player should have Points = LOVE");

        assertEquals(6, looserScore.getGames(), "Looser Player should have Games = 6");
        assertEquals(0, looserScore.getPoints().getValue(), "Looser Player should have Points = LOVE");

        assertTrue(matchScore.isTiebreak(), "isTiebreak should be true");
    }



    @Test
    void testCalcScore_MatchFinishes() {
        // Arrange
        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //тут используем реальный объект, а не заглушку потому что winner относится к MatchScore
        MatchScore matchScore = new MatchScore(playerWinner, playerLooser);

        // Initial points before winner won:
        // winner: points = 40, games = 6, sets = 1;
        // looser: points = 15, games = 2, sets = 0
        PlayerScore winnerScore = matchScore.getPlayerScoreByPlayerName("Player1");
        winnerScore.setPoints(Points.FORTY);
        winnerScore.setGames(6);
        winnerScore.setSets(1);

        PlayerScore looserScore = matchScore.getPlayerScoreByPlayerName("Player2");
        looserScore.setPoints(Points.FIFTEEN);
        looserScore.setGames(2);
        looserScore.setSets(0);

        // Act
        matchScoreCalcService.calculateScore(matchScore, "Player1", "Player2");
        matchScore.isMatchFinished();

        // Assert
        assertEquals(playerWinner, matchScore.getWinner());
    }

    @Test
    void testCalcScore_WinnerGetTiebreakPoints_WhenTiebreakPointsBelow7() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: TiebreakPoints = 3, games = 6;
        //looser: TiebreakPoints = 2, games = 6
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setTiebreakPoints(3);
        winnerScoreMock.setGames(6);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setTiebreakPoints(2);
        looserScoreMock.setGames(6);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(true);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(4, winnerScoreMock.getTiebreakPoints(), "Winner Player should have TiebreakPoints = 4");
        assertEquals(2, looserScoreMock.getTiebreakPoints(), "Looser Player should have TiebreakPoints = 2");
    }

    @Test
    void testCalcScore_WinnerGetTiebreakPoints_WhenTiebreakPointsAbove7() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: TiebreakPoints = 9, games = 6;
        //looser: TiebreakPoints = 9, games = 6
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setTiebreakPoints(9);
        winnerScoreMock.setGames(6);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setTiebreakPoints(9);
        looserScoreMock.setGames(6);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(true);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(10, winnerScoreMock.getTiebreakPoints(), "Winner Player should have TiebreakPoints = 10");
        assertEquals(6, winnerScoreMock.getGames(), "Winner Player should have Games = 6");

        assertEquals(9, looserScoreMock.getTiebreakPoints(), "Looser Player should have TiebreakPoints = 9");
        assertEquals(6, looserScoreMock.getGames(), "Looser Player should have Games = 6");
    }


    @Test
    void testCalcScore_WinnerGetSet_WhenTiebreakPointsBelow7() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: TiebreakPoints = 6, games = 6, sets = 0
        //looser: TiebreakPoints = 5, games = 6, sets = 0
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setTiebreakPoints(6);
        winnerScoreMock.setGames(6);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setTiebreakPoints(5);
        looserScoreMock.setGames(6);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(true);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(0, winnerScoreMock.getTiebreakPoints(), "Winner Player should have TiebreakPoints = 0");
        assertEquals(0, winnerScoreMock.getGames(), "Winner Player should have Games = 0");
        assertEquals(1, winnerScoreMock.getSets(), "Winner Player should have Sets = 1");

        assertEquals(0, looserScoreMock.getTiebreakPoints(), "Looser Player should have TiebreakPoints = 0");
        assertEquals(0, looserScoreMock.getGames(), "Looser Player should have Games = 0");
        assertEquals(0, looserScoreMock.getSets(), "Looser Player should have Games = 0");
    }

    @Test
    void testCalcScore_WinnerGetSet_WhenTiebreakPointsAbove7() {
        //Arrange
        MatchScore matchScoreMock = mock(MatchScore.class);

        Player playerWinner = new Player(1, "Player1");
        Player playerLooser = new Player(2, "Player2");

        //initial points before winner won:
        //winner: TiebreakPoints = 8, games = 6, sets = 0
        //looser: TiebreakPoints = 7, games = 6, sets = 0
        PlayerScore winnerScoreMock = new PlayerScore(playerWinner);
        winnerScoreMock.setTiebreakPoints(8);
        winnerScoreMock.setGames(6);
        PlayerScore looserScoreMock = new PlayerScore(playerLooser);
        looserScoreMock.setTiebreakPoints(7);
        looserScoreMock.setGames(6);

        when(matchScoreMock.getPlayerScoreByPlayerName("Player1")).thenReturn(winnerScoreMock);
        when(matchScoreMock.getPlayerScoreByPlayerName("Player2")).thenReturn(looserScoreMock);
        when(matchScoreMock.isTiebreak()).thenReturn(true);

        //Act
        matchScoreCalcService.calculateScore(matchScoreMock, "Player1", "Player2");

        //Assert
        assertEquals(0, winnerScoreMock.getTiebreakPoints(), "Winner Player should have TiebreakPoints = 0");
        assertEquals(0, winnerScoreMock.getGames(), "Winner Player should have Games = 0");
        assertEquals(1, winnerScoreMock.getSets(), "Winner Player should have Sets = 1");

        assertEquals(0, looserScoreMock.getTiebreakPoints(), "Looser Player should have TiebreakPoints = 0");
        assertEquals(0, looserScoreMock.getGames(), "Looser Player should have Games = 0");
        assertEquals(0, looserScoreMock.getSets(), "Looser Player should have Games = 0");
    }
}