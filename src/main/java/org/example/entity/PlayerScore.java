package org.example.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerScore {

    private Player player;

    private Points points;
    private int games;
    private int sets;

    private int tiebreakPoints;

    public PlayerScore(Player player) {
        this.player = player;

        points = Points.LOVE;
        games = 0;
        sets = 0;
        tiebreakPoints = 0;
    }
}
