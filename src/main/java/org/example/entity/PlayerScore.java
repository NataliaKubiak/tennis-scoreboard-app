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

    public PlayerScore(Player player) {
        this.player = player;

        points = Points.LOVE;
        games = 0;
        sets = 0;
    }
}
