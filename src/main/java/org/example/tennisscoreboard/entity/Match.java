package org.example.tennisscoreboard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player1")
    private Player player1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Player2")
    private Player player2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Winner")
    private Player winner;
}
