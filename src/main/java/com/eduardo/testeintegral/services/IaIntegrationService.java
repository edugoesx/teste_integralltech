package com.eduardo.testeintegral.services;

import com.eduardo.testeintegral.dtos.AnaliseIaResponseDTO;
import com.eduardo.testeintegral.enums.Prioridade;
import com.eduardo.testeintegral.enums.Setor;
import com.eduardo.testeintegral.models.Chamado;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class IaIntegrationService {


    @Value("${groq.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AnaliseIaResponseDTO analisarChamado(Chamado chamado) {
        String url = "https://api.groq.com/openai/v1/chat/completions";

        // 1. O segredo está no Prompt: ser muito claro e exigir JSON
        String prompt = String.format(
                "Você é um analista de triagem de TI. Analise o seguinte chamado: " +
                        "Título: '%s', Descrição: '%s'. " +
                        "Retorne APENAS um objeto JSON estrito com estas exatas chaves: " +
                        "\"prioridadeSugerida\" (valores permitidos: BAIXA, MEDIA, ALTA, CRITICA), " +
                        "\"setorSugerido\" (valores permitidos: TI, MANUTENCAO, RH, FINANCEIRO), " +
                        "\"resumo\" (máximo 2 frases). " +
                        "Não escreva nada fora do JSON.",
                chamado.getTitulo(), chamado.getDescricao()
        );

        // 2. Montar os Headers da requisição (Autorização)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 3. Montar o Corpo da requisição (Body)
        Map<String, Object> message = Map.of("role", "user", "content", prompt);
        Map<String, Object> body = Map.of(
                "model", "llama3-8b-8192", // Modelo rápido e leve da Groq
                "messages", List.of(message),
                "temperature", 0.1 // Temperatura baixa para a IA ser objetiva e não inventar formato
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // 4. Disparar a requisição para a Groq
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // 5. O Groq devolve um JSON enorme. Precisamos navegar até choices[0].message.content
            JsonNode root = objectMapper.readTree(response.getBody());
            String conteudoGeradoPelaIa = root.path("choices").get(0).path("message").path("content").asText();

            // 6. Converter o texto JSON que a IA gerou para uma classe temporária
            // Criamos um record interno rapidinho só para ler o que a IA mandou
            record RespostaIA(Prioridade prioridadeSugerida, Setor setorSugerido, String resumo) {}
            RespostaIA iaSugeriu = objectMapper.readValue(conteudoGeradoPelaIa, RespostaIA.class);

            // 7. Montar o DTO final que o nosso Controller está esperando
            return new AnaliseIaResponseDTO(
                    chamado.getId(),
                    iaSugeriu.prioridadeSugerida(),
                    iaSugeriu.setorSugerido(),
                    iaSugeriu.resumo(),
                    LocalDateTime.now()
            );

        } catch (Exception e) {
            // Se a API cair, estourar timeout ou a IA não devolver um JSON válido
            throw new RuntimeException("Erro ao consultar a inteligência artificial: " + e.getMessage());
        }
    }
}
