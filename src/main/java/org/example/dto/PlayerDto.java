package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDto {

    private Integer id;
    private String name;
}
