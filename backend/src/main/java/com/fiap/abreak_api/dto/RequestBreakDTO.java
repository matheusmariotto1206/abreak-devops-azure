package com.fiap.abreak_api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestBreakDTO(
    @NotNull(message = "Usuário é obrigatório")
    Long userId,

    @NotBlank(message = "Tipo de pausa é obrigatório")
    String breakType,

    @Min(30)
    @Max(200)
    @NotNull(message = "Duração é obrigatório")
    Integer durationSeconds
) {
    
}
