package com.fiap.abreak_api.dto;

public record UserDTO(
    Long id,
    String name,
    String email,
    String position,
    Integer breaksQuota,
    Integer breaksToday,
    String statusQuota
) {
    
}
