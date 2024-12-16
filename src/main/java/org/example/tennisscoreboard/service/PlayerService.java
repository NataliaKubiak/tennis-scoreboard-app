package org.example.tennisscoreboard.service;

import org.example.tennisscoreboard.dao.PlayerDao;
import org.example.tennisscoreboard.dto.NewPlayerDto;
import org.example.tennisscoreboard.entity.Player;
import org.example.tennisscoreboard.mapper.PlayerMapper;
import org.example.tennisscoreboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class PlayerService {

    private PlayerDao playerDao = new PlayerDao();
    private PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    public Player getOrSavePlayer(NewPlayerDto newPlayerDto) {
        Player player = playerMapper.toEntity(newPlayerDto);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            Optional<Player> maybePlayer = playerDao.getByPlayerName(player.getName(), session);

            if (maybePlayer.isPresent()) {
                return maybePlayer.get();
            }

            //тут ничего не возвращаем потому что id будет добавлено в Player, который сверху создан из DTO
            playerDao.save(player, session);
            session.getTransaction().commit();
        }

        return player;
    }
}
