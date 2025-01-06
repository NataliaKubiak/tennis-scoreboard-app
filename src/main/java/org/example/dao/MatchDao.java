package org.example.dao;

import lombok.extern.log4j.Log4j2;
import org.example.entity.Match;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

@Log4j2
public class MatchDao implements Dao<Match> {

    @Override
    public Match save(Match match) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in MatchDAO.save()");

            session.beginTransaction();
            log.info("Transaction started. Saving Match: {}", match);

            session.persist(match);

            session.getTransaction().commit();
            log.info("Transaction committed. Match saved: {}", match);

            return match;
        }
    }

    public List<Match> getAllMatches(int pageNo, int pageSize) {
//        "SELECT * FROM matches LIMIT ? OFFSET ?";
        log.info("Fetching all matches. Page: {}, Page size: {}", pageNo, pageSize);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in MatchDAO.getAllMatches()");

            session.beginTransaction();

            List<Match> matches = session.createQuery("FROM Match", Match.class)
                    .setMaxResults(pageSize)
                    .setFirstResult((pageNo - 1) * pageSize)
                    .list();

            session.getTransaction().commit();

            log.info("Successfully fetched {} matches.", matches.size());
            return matches;

        }
    }

    public int countAllMatches() {
        log.info("Counting all matches in the database.");

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in MatchDAO.countAllMatches()");

            session.beginTransaction();

            Long matchesAmount = (Long) session.createQuery("SELECT COUNT(m) FROM Match m")
                    .uniqueResult();

            session.getTransaction().commit();

            int count = matchesAmount != null ? matchesAmount.intValue() : 0;
            log.info("Total matches count: {}", count);
            return count;

        }
    }

    public List<Match> getMatchesByPlayerName(int pageNo, int pageSize, String name) {
        log.info("Fetching matches for player name: '{}'. Page: {}, Page size: {}", name, pageNo, pageSize);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in MatchDAO.getMatchesByPlayerName()");

            session.beginTransaction();

            List<Match> matches = session.createQuery(
                            "SELECT m " +
                                    "FROM Match m " +
                                    "JOIN m.player1 p1 " +
                                    "JOIN m.player2 p2 " +
                                    "WHERE p1.name = :playerName " +
                                    "OR p2.name = :playerName",
                            Match.class)
                    .setParameter("playerName", name)
                    .setMaxResults(pageSize)
                    .setFirstResult((pageNo - 1) * pageSize)
                    .list();

            session.getTransaction().commit();
            log.info("Successfully fetched {} matches for player '{}'.", matches.size(), name);
            return matches;

        }
    }

    public int countAllMatchesWithFilter(String name) {
        log.info("Counting matches for player name filter: '{}'.", name);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in MatchDAO.countAllMatchesWithFilter()");

            session.beginTransaction();

            Long matchesAmount = (Long) session.createQuery(
                            "SELECT COUNT(m) " +
                                    "FROM Match m " +
                                    "JOIN m.player1 p1 " +
                                    "JOIN m.player2 p2 " +
                                    "WHERE p1.name = :playerName " +
                                    "OR p2.name = :playerName")
                    .setParameter("playerName", name)
                    .uniqueResult();

            session.getTransaction().commit();

            int count = matchesAmount != null ? matchesAmount.intValue() : 0;
            log.info("Total matches count for player '{}': {}", name, count);
            return count;

        }
    }
}