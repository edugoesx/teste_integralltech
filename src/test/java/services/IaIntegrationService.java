package services;

import dtos.AnaliseIaResponseDTO;
import enums.Prioridade;
import models.Chamado;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class IaIntegrationService {

    public AnaliseIaResponseDTO analisarChamado(Chamado chamado) {
        // Mock: Simulando o tempo de resposta e a inteligência de uma LLM
        String resumoSugerido = "Identificado possível problema em " + chamado.getSetor() +
                ". O usuário relata: " + chamado.getTitulo();

        return new AnaliseIaResponseDTO(
                chamado.getId(),
                Prioridade.ALTA, // Simulando que a IA achou urgente
                chamado.getSetor(),
                resumoSugerido,
                LocalDateTime.now()
        );
    }
}
