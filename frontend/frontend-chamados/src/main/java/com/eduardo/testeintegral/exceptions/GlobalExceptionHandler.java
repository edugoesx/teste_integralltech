package com.eduardo.testeintegral.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata os erros de @Valid (ex: Título curto, campos em branco)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, List<String>> resposta = new HashMap<>();
        resposta.put("erros", erros);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // Trata erros de regra de negócio (ex: tentar cancelar chamado já cancelado ou ID não encontrado)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBusinessErrors(IllegalArgumentException ex) {
        Map<String, String> resposta = new HashMap<>();
        resposta.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
}
