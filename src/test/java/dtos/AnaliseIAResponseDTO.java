package dtos;

import enums.Prioridade;
import enums.Setor;

import java.time.LocalDateTime;

public record AnaliseIAResponseDTO(
        Long chamadoId,
        Prioridade prioridadeSugerida,
        Setor setorSugerido,
        String resumo,
        LocalDateTime analisadoEm
) {}
