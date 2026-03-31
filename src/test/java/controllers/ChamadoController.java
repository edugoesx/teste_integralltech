package controllers;

import dtos.AnaliseIaResponseDTO;
import dtos.ChamadoRequestDTO;
import enums.Setor;
import jakarta.validation.Valid;
import models.Chamado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import services.ChamadoService;
import services.IaIntegrationService;

import java.util.List;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;
    private final IaIntegrationService iaService;

    public ChamadoController(ChamadoService chamadoService, IaIntegrationService iaService) {
        this.chamadoService = chamadoService;
        this.iaService = iaService;
    }

    @PostMapping
    public ResponseEntity<Chamado> criar(@RequestBody @Valid ChamadoRequestDTO dto) {
        Chamado criado = chamadoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @GetMapping
    public ResponseEntity<List<Chamado>> listarTodos() {
        return ResponseEntity.ok(chamadoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    @GetMapping("/setor/{setor}")
    public ResponseEntity<List<Chamado>> buscarPorSetor(@PathVariable Setor setor) {
        return ResponseEntity.ok(chamadoService.buscarPorSetor(setor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizar(@PathVariable Long id, @RequestBody @Valid ChamadoRequestDTO dto) {
        return ResponseEntity.ok(chamadoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        chamadoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/analisar")
    public ResponseEntity<AnaliseIaResponseDTO> analisar(@PathVariable Long id) {
        Chamado chamado = chamadoService.buscarPorId(id);
        AnaliseIaResponseDTO analise = iaService.analisarChamado(chamado);
        return ResponseEntity.ok(analise);
    }
}
