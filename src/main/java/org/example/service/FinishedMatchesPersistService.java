package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.dao.MatchDao;
import org.example.dto.MatchDto;
import org.example.entity.Match;
import org.example.entity.MatchScore;
import org.example.mapper.MatchMapper;
import org.example.mapper.MatchScoreToMatchMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

@Log4j2
public class FinishedMatchesPersistService {

    private final MatchDao matchDao = new MatchDao();
    private final MatchScoreToMatchMapper matchScoreToMatchMapper = MatchScoreToMatchMapper.INSTANCE;
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    public MatchDto saveMatch(MatchScore matchScore) {
        log.info("Received MatchScore: {}", matchScore);
        Match matchEntity = matchScoreToMatchMapper.toEntity(matchScore);

        log.info("Saving Match: {}", matchEntity);
        matchDao.save(matchEntity);

        return matchMapper.toDto(matchEntity);
    }
}
