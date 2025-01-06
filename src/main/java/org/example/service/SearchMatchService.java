package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.dao.MatchDao;
import org.example.dto.MatchDto;
import org.example.entity.Match;
import org.example.mapper.MatchMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class SearchMatchService {

    private final MatchDao matchDao = new MatchDao();
    private final MatchMapper matchMapper = MatchMapper.INSTANCE;

    public List<MatchDto> searchMatchesByPlayerName(String playerName, int pageNo, int pageSize) {
        log.info("Searching matches. Player name: '{}', Page: {}, Page size: {}", playerName, pageNo, pageSize);

        List<Match> matches;

        if (playerName != null && !playerName.isEmpty()) {
            matches = matchDao.getMatchesByPlayerName(pageNo, pageSize, playerName);

        } else {
            matches = matchDao.getAllMatches(pageNo, pageSize);
        }

        log.info("Successfully retrieved {} matches.", matches.size());

        return matchMapper.toDtoList(matches);
    }

    public int getTotalRecordsAmount(String filter) {
        log.info("Calculating total records. Filter: '{}'", filter);
        int totalRecords;

        if (filter != null && !filter.isEmpty()) {
            totalRecords = matchDao.countAllMatchesWithFilter(filter);

        } else {
            totalRecords = matchDao.countAllMatches();
        }

        log.info("Total records calculated: {}", totalRecords);

        return totalRecords;
    }
}
