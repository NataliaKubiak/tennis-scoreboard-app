package org.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
@Builder
@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    // TODO: 14/12/2024 вопрос нужен ли мне лист тут или собрать его на уровне сервиса и запихать в DTO?
//    @OneToMany(cascade = CascadeType.ALL)
//    private List<Match> matches;
}
