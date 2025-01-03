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

    private Player winner;

    private boolean tiebreak;

    private final HashMap<Player, PlayerScore> matchScore = new HashMap<>();

    public MatchScore(Player player1, Player player2) {
        uuid = UUID.randomUUID();

        this.player1 = player1;
        this.player2 = player2;

        tiebreak = false;

        matchScore.put(player1, new PlayerScore(player1));
        matchScore.put(player2, new PlayerScore(player2));
    }

    public PlayerScore getPlayerScoreByPlayerName(String name) {
        if (player1.getName().equals(name)) {
            return matchScore.get(player1);
        } else {
            return matchScore.get(player2);
        }
    }

    public boolean isMatchFinished() {
        if (matchScore.get(player1).getSets() == 2) {
            winner = player1;
            return true;

        } else if (matchScore.get(player2).getSets() == 2) {
            winner = player2;
            return true;

        } else {
            return false;
        }
    }

    public Optional<UUID> getIdByPlayersNames(Player player1, Player player2) {
        if (matchScore.get(player1) != null && matchScore.get(player2) != null) {
            return Optional.of(uuid);
        } else {
            return Optional.empty();
        }
    }
}
