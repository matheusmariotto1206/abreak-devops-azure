package com.fiap.abreak_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestUserDTO(
        @NotBlank(message = "Nome é obrigatório") @Size(min = 3, max = 100) String name,

        @Email(message = "Email inválido") @NotBlank(message = "Email é obrigatório") String email,

        @NotBlank(message = "Senha é obrigatória") @Size(min = 6, message = "Senha deve ter ao minimo 6 caracteres") String password,

        String position,

        @Min(1) @Max(20) Integer breaksQuota 

) {

    public RequestUserDTO {
        if (breaksQuota == null) {
            breaksQuota = 6;
        }
    }
}
