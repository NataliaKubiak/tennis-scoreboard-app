package org.example.mapper;

import org.example.dto.NewPlayerDto;
import org.example.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    NewPlayerDto toDto(Player player);

    Player toEntity(NewPlayerDto newPlayerDto);
}
