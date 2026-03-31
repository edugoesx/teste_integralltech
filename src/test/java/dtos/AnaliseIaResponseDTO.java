package dtos;

import enums.Setor;

import java.time.LocalDateTime;

public record AnaliseIaResponseDTO(
        Long chamadoId,
        enums.Prioridade prioridadeSugerida,
        Setor setorSugerido,
        String resumo,
        LocalDateTime analisadoEm
) {}
