package com.eduardo.testeintegral.dtos;


import com.eduardo.testeintegral.enums.Prioridade;
import com.eduardo.testeintegral.enums.Setor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChamadoRequestDTO(
        @NotBlank(message = "campo obrigatório")
        @Size(min = 5, message = "deve ter no mínimo 5 caracteres")
        String titulo,

        @NotBlank(message = "campo obrigatório")
        String descricao,

        @NotNull(message = "campo obrigatório")
        Setor setor,

        @NotNull(message = "campo obrigatório")
        Prioridade prioridade,

        @NotBlank(message = "campo obrigatório")
        String solicitante
) {}
