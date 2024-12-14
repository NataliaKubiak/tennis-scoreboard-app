package org.example.tennisscoreboard.dao;

import jakarta.persistence.NoResultException;
import org.example.tennisscoreboard.entity.Match;
import org.example.tennisscoreboard.entity.Player;
import org.example.tennisscoreboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class PlayerDao implements Dao<Player> {

    @Override
    public Player save(Player player, Session session) {

        session.persist(player);

        return player;
    }

    @Override
    public Optional<Player> getByPlayerName(String name, Session session) {
        try {
            Player player = session.createQuery(
                    "SELECT p FROM Player p WHERE p.name = :name", Player.class)
                    .setParameter("name", name)
                    .getSingleResult();

            return Optional.ofNullable(player);

        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
