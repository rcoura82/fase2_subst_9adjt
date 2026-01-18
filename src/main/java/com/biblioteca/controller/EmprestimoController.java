package com.biblioteca.controller;

import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para operações com Empréstimos
 * Data e hora padrão: America/Sao_Paulo (GMT-3/-5)
 */
@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
@Tag(name = "Empréstimos", description = "API para gerenciar empréstimos de livros")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    @Operation(summary = "Criar novo empréstimo", description = "Registra um novo empréstimo de livro para um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Emprestimo.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou limite excedido"),
            @ApiResponse(responseCode = "404", description = "Livro ou usuário não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Emprestimo> criar(@Valid @RequestBody EmprestimoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(emprestimoService.criar(dto));
    }

    @PatchMapping("/{id}/devolver")
    @Operation(summary = "Devolver livro", description = "Registra a devolução de um livro emprestado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro devolvido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Empréstimo não está ativo")
    })
    public ResponseEntity<Emprestimo> devolver(@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.devolver(id));
    }

    @PatchMapping("/{id}/renovar")
    @Operation(summary = "Renovar empréstimo", description = "Estende a data de devolução de um empréstimo ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo renovado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado"),
            @ApiResponse(responseCode = "400", description = "Empréstimo não pode ser renovado")
    })
    public ResponseEntity<Emprestimo> renovar(@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.renovar(id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar empréstimo por ID", description = "Retorna os detalhes de um empréstimo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo encontrado"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.buscarPorId(id));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar empréstimos ativos", description = "Lista todos os empréstimos pendentes de devolução")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos ativos")
    })
    public ResponseEntity<Page<Emprestimo>> listarAtivos(Pageable pageable) {
        return ResponseEntity.ok(emprestimoService.listarAtivos(pageable));
    }

    @GetMapping("/atrasados")
    @Operation(summary = "Listar empréstimos atrasados", description = "Lista empréstimos com data de devolução vencida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos atrasados")
    })
    public ResponseEntity<Page<Emprestimo>> listarAtrasados(Pageable pageable) {
        return ResponseEntity.ok(emprestimoService.listarAtrasados(pageable));
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Empréstimos de um usuário", description = "Lista todos os empréstimos de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos do usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Page<Emprestimo>> listarPorUsuario(
            @PathVariable Long usuarioId,
            @Parameter(description = "Status do empréstimo")
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.ok(emprestimoService.listarPorUsuario(usuarioId, status, pageable));
    }

    @GetMapping("/livro/{livroId}")
    @Operation(summary = "Empréstimos de um livro", description = "Lista todos os empréstimos de um livro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos do livro"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Page<Emprestimo>> listarPorLivro(
            @PathVariable Long livroId,
            Pageable pageable) {
        return ResponseEntity.ok(emprestimoService.listarPorLivro(livroId, pageable));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Empréstimos por período", description = "Lista empréstimos entre duas datas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos no período")
    })
    public ResponseEntity<Page<Emprestimo>> listarPorPeriodo(
            @Parameter(description = "Data inicial (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Pageable pageable) {
        return ResponseEntity.ok(emprestimoService.listarPorPeriodo(dataInicio, dataFim, pageable));
    }

    @GetMapping("/usuario/{usuarioId}/historico")
    @Operation(summary = "Histórico de empréstimos", description = "Retorna o histórico completo de empréstimos de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de empréstimos retornado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<Emprestimo>> obterHistorico(
            @PathVariable Long usuarioId,
            @Parameter(description = "Data inicial (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @Parameter(description = "Data final (YYYY-MM-DD)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        
        LocalDate inicio = dataInicio != null ? dataInicio : LocalDate.now().minusYears(1);
        LocalDate fim = dataFim != null ? dataFim : LocalDate.now();
        
        return ResponseEntity.ok(emprestimoService.obterHistoricoUsuario(usuarioId, inicio, fim));
    }

    @GetMapping("/usuario/{usuarioId}/ativos")
    @Operation(summary = "Empréstimos ativos de um usuário", description = "Lista apenas os empréstimos ativos de um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empréstimos ativos do usuário"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<List<Emprestimo>> obterEmprestimosAtivos(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(emprestimoService.obterEmprestimosAtivosDoUsuario(usuarioId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar empréstimo", description = "Remove um registro de empréstimo (apenas administrativo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empréstimo deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        emprestimoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
