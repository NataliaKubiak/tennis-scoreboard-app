package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.dao.MatchDao;
import org.example.entity.Match;
import org.example.entity.MatchScore;
import org.example.mapper.MatchMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

@Log4j2
public class FinishedMatchesPersistService {

    private MatchDao matchDao = new MatchDao();
    private MatchMapper matchMapper = MatchMapper.INSTANCE;

    public Match saveMatch(MatchScore matchScore) {
        log.info("Received MatchScore: {}", matchScore);
        Match matchEntity = matchMapper.toEntity(matchScore);

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in FinishedMatchesPersistService.");

            session.beginTransaction();
            log.info("Transaction started.");

            log. info("Saving Match: {}", matchEntity);
            matchDao.save(matchEntity, session);
            session.getTransaction().commit();
            log.info("Transaction commited. Match saved: {}", matchEntity);

        } catch (Exception e) {
            log.error("Error during Match saving", e);
        }

        return matchEntity;
    }
}
