package org.example.mapper;

import org.example.entity.Match;
import org.example.entity.MatchScore;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchScoreToMatchMapper {
    MatchScoreToMatchMapper INSTANCE = Mappers.getMapper(MatchScoreToMatchMapper.class);

//    @Mapping(target = "uuid", ignore = true)
//    @Mapping(target = "tiebreak", ignore = true)
    Match toEntity(MatchScore matchScore);
}
