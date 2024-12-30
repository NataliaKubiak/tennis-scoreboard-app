package org.example.mapper;

import org.example.dto.MatchDto;
import org.example.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchDto toDto(Match match);

    Match toEntity(MatchDto matchDto);
}
