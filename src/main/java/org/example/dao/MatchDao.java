package org.example.dao;

import lombok.extern.log4j.Log4j2;
import org.example.entity.Match;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

@Log4j2
public class MatchDao implements Dao<Match> {

    @Override
    public Match save(Match match, Session session) {
        log.info("Saving Match: {}", match);

        session.persist(match);

        log.info("Match saved: {}", match);
        return match;
    }

    public List<Match> getMatchesByPlayerName(String name, Session session) {
        log.info("Searching for Match with Player name: {}", name);

        List<Match> matches = session.createQuery(
                        "SELECT m " +
                                "FROM Match m " +
                                "JOIN Player p1 " +
                                "JOIN Player p2 " +
                                "WHERE p1.name = :playerName " +
                                "OR p2.name = :playerName",
                        Match.class)
                .setParameter("playerName", name)
                .list();

        return matches;
    }
}