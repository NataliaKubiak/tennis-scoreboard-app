package org.example.dto;

import lombok.Data;
import org.example.entity.Player;

@Data
public class MatchDto {

    private Player player1;
    private Player player2;
    private Player winner;
}
