package com.fiap.abreak_api.dto;

public record BreakDTO(
    Long id,
    UserDTO user,
    BreakType breakType,
    String durationFormatted
) {
    
}
