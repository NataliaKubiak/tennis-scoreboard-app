package org.example.mapper;

import org.example.dto.MatchDto;
import org.example.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchDto toDto(Match match);

    Match toEntity(MatchDto matchDto);

    List<MatchDto> toDtoList(List<Match> matches);
}
