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

    private final PlayerDao playerDao = new PlayerDao();
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    public PlayerDto getOrSavePlayer(PlayerDto playerDto) {
        log.info("Received new player DTO: {}", playerDto);
        Player player = playerMapper.toEntity(playerDto);

        Optional<Player> maybePlayer = playerDao.getPlayerByName(player.getName());

        if (maybePlayer.isPresent()) {
            log.info("Player with name '{}' already exists: {}", player.getName(), maybePlayer.get());

            return playerMapper.toDto(maybePlayer.get());
        }

        //тут ничего не возвращаем потому что id будет добавлено в Player, который сверху создан из DTO
        log.info("Player with name '{}' not found, saving new player.", player.getName());
        playerDao.save(player);

        return playerMapper.toDto(player);
    }
}
