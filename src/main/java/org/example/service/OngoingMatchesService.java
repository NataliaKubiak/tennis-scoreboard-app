package org.example.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.entity.MatchScore;
import org.example.entity.Player;
import org.example.entity.PlayerScore;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OngoingMatchesService {

    private static final OngoingMatchesService INSTANCE = new OngoingMatchesService();

    private final int MAP_SIZE = 20;
//    @Getter
    private ConcurrentMap<UUID, MatchScore> ongoingMatches = new ConcurrentHashMap<>(MAP_SIZE);
    private MatchScoreCalcService matchScoreCalcService = new MatchScoreCalcService();

    public static OngoingMatchesService getInstance() {
        return INSTANCE;
    }

    public UUID addNewMatchToMap(Player player1, Player player2) {
        MatchScore matchScore = new MatchScore(player1, player2);
        UUID matchId = matchScore.getUuid();

        ongoingMatches.putIfAbsent(matchId, matchScore);
        return matchId;
    }

    public List<String> getPlayersNamesByMatchId(UUID id) {
        MatchScore matchScore = ongoingMatches.get(id);

        return List.of(
                matchScore.getPlayer1().getName(),
                matchScore.getPlayer2().getName()
        );
    }

    public List<Player> getPlayersByMatchId(UUID id) {
        MatchScore matchScore = ongoingMatches.get(id);

        return List.of(
                matchScore.getPlayer1(),
                matchScore.getPlayer2()
        );
    }

    public MatchScore updateMatchScoreWithId(UUID id, String winnerName, String looserName) {
        MatchScore matchScore = ongoingMatches.get(id);
        PlayerScore winnerScore = matchScore.getPlayerScoreByPlayerName(winnerName);
        PlayerScore looserScore = matchScore.getPlayerScoreByPlayerName(looserName);

//        Point currentWinnerPoints = winnerScore.getPoints();
//        Point currentLooserPoints = looserScore.getPoints();
        matchScoreCalcService.calculateScore(winnerScore, looserScore);

//        winnerScore.setPoints(currentWinnerPoints.next());

        return matchScore;
    }
}
