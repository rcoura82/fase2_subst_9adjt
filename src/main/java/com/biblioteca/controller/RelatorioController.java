package com.biblioteca.controller;

import com.biblioteca.service.RelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para geração de relatórios
 * Utiliza Java Streams para processamento eficiente
 */
@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@Tag(name = "Relatórios", description = "API para gerar relatórios da biblioteca")
public class RelatorioController {

    private final RelatorioService relatorioService;

    @GetMapping("/top-20-livros-emprestados")
    @Operation(summary = "Top 20 livros mais emprestados", 
            description = "Retorna os 20 livros mais emprestados da biblioteca usando Streams")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<List<RelatorioService.LivroComContagemDTO>> relatorio20LivrosMaisEmprestados() {
        return ResponseEntity.ok(relatorioService.relatorio20LivrosMaisEmprestados());
    }

    @GetMapping("/livros-emprestados")
    @Operation(summary = "Livros emprestados com previsão de devolução", 
            description = "Lista todos os livros emprestados com data de devolução prevista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<List<RelatorioService.LivroEmprestadoDTO>> relatorioLivrosEmprestados() {
        return ResponseEntity.ok(relatorioService.relatorioLivrosEmprestados());
    }

    @GetMapping("/emprestimos-por-usuario")
    @Operation(summary = "Empréstimos por usuário", 
            description = "Resumo de empréstimos agrupado por usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<List<RelatorioService.UsuarioEmprestimosDTO>> relatorioEmprestimosPorUsuario() {
        return ResponseEntity.ok(relatorioService.relatorioEmprestimosPorUsuario());
    }

    @GetMapping("/livros-por-categoria")
    @Operation(summary = "Livros por categoria", 
            description = "Relatório com estatísticas de livros agrupados por categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<List<RelatorioService.LivroPorCategoriaDTO>> relatorioLivrosPorCategoria() {
        return ResponseEntity.ok(relatorioService.relatorioLivrosPorCategoria());
    }

    @GetMapping("/atividade-periodo")
    @Operation(summary = "Atividade em período", 
            description = "Relatório de atividades (empréstimos e devoluções) em um período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
    })
    public ResponseEntity<RelatorioService.RelatorioAtividadeDTO> relatorioAtividadePeriodo(
            @Parameter(description = "Data inicial (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusMonths(1);
        LocalDate fim = dataFim != null ? dataFim : LocalDate.now();
        
        return ResponseEntity.ok(relatorioService.relatorioAtividadePeriodo(inicio, fim));
    }
}
