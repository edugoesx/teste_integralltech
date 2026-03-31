package services;

import dtos.ChamadoRequestDTO;
import enums.Setor;
import enums.Status;
import models.Chamado;
import org.springframework.stereotype.Service;
import repositories.ChamadoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChamadoService {

    private final ChamadoRepository repository;

    // Injeção de dependência via construtor (melhor prática)
    public ChamadoService(ChamadoRepository repository) {
        this.repository = repository;
    }

    public Chamado criar(ChamadoRequestDTO dto) {
        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setSetor(dto.setor());
        chamado.setPrioridade(dto.prioridade());
        chamado.setSolicitante(dto.solicitante());

        // Regras de negócio da criação
        chamado.setStatus(Status.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());

        return repository.save(chamado);
    }

    public List<Chamado> listarTodos() {
        return repository.findAll();
    }

    public Chamado buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chamado não encontrado"));
    }

    public List<Chamado> buscarPorSetor(Setor setor) {
        return repository.findBySetor(setor);
    }

    public Chamado atualizar(Long id, ChamadoRequestDTO dto) {
        Chamado chamado = buscarPorId(id);

        if (chamado.getStatus() == Status.CANCELADO || chamado.getStatus() == Status.RESOLVIDO) {
            throw new IllegalArgumentException("Não é possível alterar um chamado " + chamado.getStatus());
        }

        chamado.setTitulo(dto.titulo());
        chamado.setDescricao(dto.descricao());
        chamado.setSetor(dto.setor());
        chamado.setPrioridade(dto.prioridade());

        return repository.save(chamado);
    }

    public Chamado cancelar(Long id) {
        Chamado chamado = buscarPorId(id);

        // Regra: Não pode reabrir ou cancelar de novo um chamado já finalizado
        if (chamado.getStatus() == Status.CANCELADO || chamado.getStatus() == Status.RESOLVIDO) {
            throw new IllegalArgumentException("O chamado já está encerrado ou cancelado.");
        }

        // Regra: DELETE não deleta, apenas muda o status
        chamado.setStatus(Status.CANCELADO);
        chamado.setDataFechamento(LocalDateTime.now());

        return repository.save(chamado);
    }
}
