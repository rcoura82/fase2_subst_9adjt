package com.biblioteca.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para criar um Empréstimo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmprestimoDTO {

    @Schema(description = "ID do empréstimo", example = "1")
    private Long id;

    @NotNull(message = "ID do livro é obrigatório")
    @Schema(description = "ID do livro a emprestar", example = "1")
    private Long livroId;

    @NotNull(message = "ID do usuário é obrigatório")
    @Schema(description = "ID do usuário que está pegando o livro", example = "1")
    private Long usuarioId;

    @Schema(description = "Data do empréstimo (YYYY-MM-DD)", example = "2024-01-18")
    private LocalDate dataEmprestimo;

    @Schema(description = "Data prevista de devolução (YYYY-MM-DD)", example = "2024-02-01")
    private LocalDate dataDeVolucaoPrevista;

    @Schema(description = "Data real de devolução (YYYY-MM-DD)")
    private LocalDate dataDeVolucaoReal;

    @Schema(description = "Status do empréstimo", example = "ATIVO", allowableValues = {"ATIVO", "DEVOLVIDO", "ATRASADO", "CANCELADO"})
    private String status;

    @Schema(description = "Observações sobre o empréstimo")
    private String observacoes;

    @Schema(description = "Dias em atraso (apenas leitura)")
    private Long diasAtrasados;
}
