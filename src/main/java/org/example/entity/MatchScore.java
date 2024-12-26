package org.example.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Data
public class MatchScore {

    private UUID uuid;

    private Player player1;
    private Player player2;

    private final HashMap<Player, PlayerScore> matchScore = new HashMap<>();

    public MatchScore(Player player1, Player player2) {
        uuid = UUID.randomUUID();

        this.player1 = player1;
        this.player2 = player2;

        matchScore.put(player1, new PlayerScore(player1));
        matchScore.put(player2, new PlayerScore(player2));
    }

//    public UUID createNewMatch(Player player1, Player player2) {
//        uuid = UUID.randomUUID();
//
//        this.player1 = player1;
//        this.player2 = player2;
//
//        matchScore.put(player1, new PlayerScore(player1));
//        matchScore.put(player2, new PlayerScore(player2));
//
//        return uuid;
//    }

//    public Optional<UUID> getIdByPlayersNames(Player player1, Player player2) {
//        if (matchScore.get(player1) != null && matchScore.get(player2) != null) {
//            return Optional.of(uuid);
//        } else {
//            return Optional.empty();
//        }
//    }
}
