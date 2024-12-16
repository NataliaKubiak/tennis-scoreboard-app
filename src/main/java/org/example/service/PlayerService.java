package org.example.service;

import org.example.dao.PlayerDao;
import org.example.dto.NewPlayerDto;
import org.example.entity.Player;
import org.example.mapper.PlayerMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

public class PlayerService {

    private PlayerDao playerDao = new PlayerDao();
    private PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    public Player getOrSavePlayer(NewPlayerDto newPlayerDto) {
        Player player = playerMapper.toEntity(newPlayerDto);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

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
