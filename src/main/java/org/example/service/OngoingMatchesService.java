package org.example.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.example.entity.MatchScore;
import org.example.entity.Player;

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
}