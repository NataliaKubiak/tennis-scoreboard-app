package org.example.tennisscoreboard.service;

import org.example.tennisscoreboard.dao.PlayerDao;
import org.example.tennisscoreboard.entity.Player;
import org.example.tennisscoreboard.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Optional;

public class PlayerService {

    private PlayerDao playerDao = new PlayerDao();

    public Player getOrSavePlayer(Player player) {
        // TODO: 14/12/2024 тут надо передать параметром PlayerDto и написать PlayerDto -> Player

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
