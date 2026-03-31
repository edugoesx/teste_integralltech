package models;

import enums.Prioridade;
import enums.Setor;
import enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Chamado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 5, message = "deve ter no mínimo 5 caracteres")
    private String titulo;

    @NotBlank(message = "campo obrigatório")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Setor setor;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    @NotBlank(message = "campo obrigatório")
    private String solicitante;

}