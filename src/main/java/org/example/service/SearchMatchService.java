package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.dao.MatchDao;
import org.example.entity.Match;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class SearchMatchService {

    MatchDao matchDao = new MatchDao();

    public List<Match> searchMatchesByPlayerName(String playerName, int pageNo, int pageSize) {
        log.info("Searching matches. Player name: '{}', Page: {}, Page size: {}", playerName, pageNo, pageSize);

        List<Match> matches = new ArrayList<>();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            if (playerName != null && !playerName.isEmpty()) {
                matches = matchDao.getMatchesByPlayerName(session, pageNo, pageSize, playerName);

            } else {
                matches = matchDao.getAllMatches(session, pageNo, pageSize);
            }

            session.getTransaction().commit();
            log.info("Successfully retrieved {} matches.", matches.size());

        } catch (Exception e) {
            log.error("Error during Search Matches", e);
        }

        return matches;
    }

    public int getTotalRecordsAmount(String filter) {
        log.info("Calculating total records. Filter: '{}'", filter);
        int totalRecords = 0;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            if (filter != null && !filter.isEmpty()) {
                totalRecords = matchDao.countAllMatchesWithFilter(session, filter);

            } else {
                totalRecords = matchDao.countAllMatches(session);
            }

            session.getTransaction().commit();
            log.info("Total records calculated: {}", totalRecords);

        } catch (Exception e) {
            log.error("Error during Search Total Matches Amount", e);
        }

        return totalRecords;
    }
}
