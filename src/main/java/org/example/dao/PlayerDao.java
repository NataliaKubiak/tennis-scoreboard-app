package org.example.dao;

import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.example.entity.Player;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@Log4j2
public class PlayerDao implements Dao<Player> {

    public List<Player> getAll(Session session) {
        return session.createQuery("FROM Player", Player.class).list();
    }

    @Override
    public Player save(Player player, Session session) {
        log.info("Saving player: {}", player);

        session.persist(player);

        log.info("Player saved: {}", player);
        return player;
    }

    public Optional<Player> getPlayerByName(String name, Session session) {
        log.info("Searching for player with name: {}", name);

        try {
            Player player = session.createQuery(
                            "SELECT p " +
                                    "FROM Player p " +
                                    "WHERE p.name = :name",
                            Player.class)
                    .setParameter("name", name)
                    .getSingleResult();

            log.info("Player found: {}", player);
            return Optional.of(player);

        } catch (NoResultException e) {
            log.info("Player with name '{}' not found.", name);
            return Optional.empty();
        }
    }
}
