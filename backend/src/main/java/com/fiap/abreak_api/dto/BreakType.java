package com.fiap.abreak_api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BreakType {
    HIDRATACAO("Hidratação"),
    ALONGAMENTO("Alongamento"),
    DESCANSO_VISUAL("Descanso Visual");

    private final String description;

}