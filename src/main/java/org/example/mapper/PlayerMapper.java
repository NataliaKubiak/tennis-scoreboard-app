package org.example.mapper;

import org.example.dto.PlayerDto;
import org.example.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    PlayerDto toDto(Player player);

    Player toEntity(PlayerDto playerDto);
}
