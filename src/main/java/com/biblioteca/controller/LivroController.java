package com.biblioteca.controller;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.entity.Livro;
import com.biblioteca.service.LivroService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações com Livros
 */
@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "API para gerenciar livros da biblioteca")
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    @Operation(summary = "Criar novo livro", description = "Cadastra um novo livro na biblioteca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou livro duplicado"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Livro> criar(@Valid @RequestBody LivroDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza os dados de um livro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Livro> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LivroDTO dto) {
        return ResponseEntity.ok(livroService.atualizar(id, dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar livro por ID", description = "Retorna os detalhes de um livro específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(livroService.buscarPorId(id));
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Buscar livro por ISBN", description = "Retorna um livro usando seu ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<Livro> buscarPorIsbn(@PathVariable String isbn) {
        return ResponseEntity.ok(livroService.buscarPorIsbn(isbn));
    }

    @GetMapping
    @Operation(summary = "Listar livros", description = "Lista todos os livros com paginação e filtros opcionais")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de livros retornada com sucesso")
    })
    public ResponseEntity<Page<Livro>> listar(
            @Parameter(description = "Filtro por título (parcial)")
            @RequestParam(required = false) String titulo,
            @Parameter(description = "Filtro por autor (parcial)")
            @RequestParam(required = false) String autor,
            @Parameter(description = "Filtro por ISBN (exato)")
            @RequestParam(required = false) String isbn,
            @Parameter(description = "Filtro por categoria")
            @RequestParam(required = false) String categoria,
            @ParameterObject Pageable pageable) {
        
        if (titulo != null || autor != null || isbn != null || categoria != null) {
            return ResponseEntity.ok(livroService.buscarComFiltros(titulo, autor, isbn, categoria, pageable));
        }
        return ResponseEntity.ok(livroService.listarTodos(pageable));
    }

    @GetMapping("/disponíveis")
    @Operation(summary = "Listar livros disponíveis", description = "Lista apenas livros com cópias em estoque")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de livros disponíveis")
    })
    public ResponseEntity<Page<Livro>> listarDisponiveis(Pageable pageable) {
        return ResponseEntity.ok(livroService.buscarDisponiveis(pageable));
    }

    @GetMapping("/mais-emprestados")
    @Operation(summary = "Top 20 livros mais emprestados", description = "Retorna os 20 livros mais emprestados da biblioteca")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista dos 20 livros mais emprestados")
    })
    public ResponseEntity<List<Livro>> obterTop20MaisEmprestados() {
        return ResponseEntity.ok(livroService.obterTop20MaisEmprestados());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar livro", description = "Remove um livro do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Livro possui empréstimos")
    })
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        livroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
