package org.example.tennisscoreboard.mapper;

import org.example.tennisscoreboard.dto.NewPlayerDto;
import org.example.tennisscoreboard.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    NewPlayerDto toDto(Player player);

    Player toEntity(NewPlayerDto newPlayerDto);
}
