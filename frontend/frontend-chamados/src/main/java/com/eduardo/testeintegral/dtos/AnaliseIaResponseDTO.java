package com.eduardo.testeintegral.dtos;

import com.eduardo.testeintegral.enums.Prioridade;
import com.eduardo.testeintegral.enums.Setor;

import java.time.LocalDateTime;

public record AnaliseIaResponseDTO(
        Long chamadoId,
        Prioridade prioridadeSugerida,
        Setor setorSugerido,
        String resumo,
        LocalDateTime analisadoEm
) {}
