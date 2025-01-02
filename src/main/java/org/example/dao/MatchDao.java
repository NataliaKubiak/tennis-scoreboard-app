package org.example.dao;

import lombok.extern.log4j.Log4j2;
import org.example.entity.Match;
import org.hibernate.Session;

import java.util.Collections;
import java.util.List;

@Log4j2
public class MatchDao implements Dao<Match> {

    @Override
    public Match save(Match match, Session session) {
        log.info("Saving Match: {}", match);

        session.persist(match);

        log.info("Match saved: {}", match);
        return match;
    }

    public List<Match> getAllMatches(Session session, int pageNo, int pageSize) {
//        "SELECT * FROM matches LIMIT ? OFFSET ?";
        log.info("Fetching all matches. Page: {}, Page size: {}", pageNo, pageSize);

        try {
            List<Match> matches = session.createQuery("FROM Match", Match.class)
                    .setMaxResults(pageSize)
                    .setFirstResult((pageNo - 1) * pageSize)
                    .list();
            log.info("Successfully fetched {} matches.", matches.size());
            return matches;

        } catch (Exception e) {
            log.error("Error while fetching all matches", e);
            return Collections.emptyList();
        }
    }

    public int countAllMatches(Session session) {
        log.info("Counting all matches in the database.");

        try {
            Long matchesAmount = (Long) session.createQuery("SELECT COUNT(m) FROM Match m")
                    .uniqueResult();
            int count = matchesAmount != null ? matchesAmount.intValue() : 0;
            log.info("Total matches count: {}", count);
            return count;

        } catch (Exception e) {
            log.error("Error while counting all matches", e);
            return 0;
        }
    }

    public List<Match> getMatchesByPlayerName(Session session, int pageNo, int pageSize, String name) {
        log.info("Fetching matches for player name: '{}'. Page: {}, Page size: {}", name, pageNo, pageSize);

        try {
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
            log.info("Successfully fetched {} matches for player '{}'.", matches.size(), name);
            return matches;

        } catch (Exception e) {
            log.error("Error while fetching matches for player '{}'", name, e);
            return Collections.emptyList();
        }
    }

    public int countAllMatchesWithFilter(Session session, String name) {
        log.info("Counting matches for player name filter: '{}'.", name);

        try {
            Long matchesAmount = (Long) session.createQuery(
                            "SELECT COUNT(m) " +
                                    "FROM Match m " +
                                    "JOIN m.player1 p1 " +
                                    "JOIN m.player2 p2 " +
                                    "WHERE p1.name = :playerName " +
                                    "OR p2.name = :playerName")
                    .setParameter("playerName", name)
                    .uniqueResult();
            int count = matchesAmount != null ? matchesAmount.intValue() : 0;
            log.info("Total matches count for player '{}': {}", name, count);
            return count;
        } catch (Exception e) {
            log.error("Error while counting matches for player '{}'", name, e);
            return 0;
        }
    }
}