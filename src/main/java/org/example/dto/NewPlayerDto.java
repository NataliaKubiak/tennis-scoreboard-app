package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class NewPlayerDto {

    // TODO: 15/12/2024 может быть id поле убрать?
    private Integer id;
    private String name;
}
