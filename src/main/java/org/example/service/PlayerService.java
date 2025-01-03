package org.example.service;

import lombok.extern.log4j.Log4j2;
import org.example.dao.PlayerDao;
import org.example.dto.PlayerDto;
import org.example.entity.Player;
import org.example.mapper.PlayerMapper;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.Optional;

@Log4j2
public class PlayerService {

    private PlayerDao playerDao = new PlayerDao();
    private PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    public PlayerDto getOrSavePlayer(PlayerDto playerDto) {
        log.info("Received new player DTO: {}", playerDto);
        Player player = playerMapper.toEntity(playerDto);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            log.info("Opened Hibernate session in PlayerService.");

            session.beginTransaction();
            log.info("Transaction started.");

            Optional<Player> maybePlayer = playerDao.getPlayerByName(player.getName(), session);

            if (maybePlayer.isPresent()) {
                log.info("Player with name '{}' already exists: {}", player.getName(), maybePlayer.get());

                return playerMapper.toDto(maybePlayer.get());
            }

            //тут ничего не возвращаем потому что id будет добавлено в Player, который сверху создан из DTO
            log.info("Player with name '{}' not found, saving new player.", player.getName());
            playerDao.save(player, session);
            session.getTransaction().commit();
            log.info("Transaction committed. Player saved: {}", player);

        } catch (Exception e) {
            log.error("Error during player retrieval or saving", e);
        }

        return playerMapper.toDto(player);
    }
}
