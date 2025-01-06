package org.example.dao;

import jakarta.persistence.NoResultException;
import lombok.extern.log4j.Log4j2;
import org.example.entity.Player;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@Log4j2
public class PlayerDao implements Dao<Player> {

    public List<Player> getAll(Session session) {
        return session.createQuery("FROM Player", Player.class).list();
    }

    @Override
    public Player save(Player player) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in PlayerDAO.save()");

            session.beginTransaction();
            log.info("Transaction started. Saving player: {}", player);

            session.persist(player);

            session.getTransaction().commit();
            log.info("Transaction committed. Player saved: {}", player);

            return player;

        } catch (Exception ex) {
            log.error("Error during Player Saving: {}, {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public Optional<Player> getPlayerByName(String name) {
        log.info("Searching for player with name: {}", name);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in PlayerDAO.getPlayerByName()");

            session.beginTransaction();
            log.info("Transaction started.");

            Optional<Player> optionalPlayer = session.createQuery(
                            "SELECT p " +
                                    "FROM Player p " +
                                    "WHERE p.name = :name",
                            Player.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();

            session.getTransaction().commit();
            log.info("Transaction committed.");

            return optionalPlayer;

        } catch (Exception ex) {
            log.error("Error during Searching for player: {}, {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
