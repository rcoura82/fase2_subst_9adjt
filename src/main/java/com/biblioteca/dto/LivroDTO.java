package com.biblioteca.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criar ou atualizar um Livro
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LivroDTO {

    @Schema(description = "ID do livro", example = "1")
    private Long id;

    @NotBlank(message = "Título é obrigatório")
    @Schema(description = "Título do livro", example = "Clean Code")
    private String titulo;

    @NotBlank(message = "Autor é obrigatório")
    @Schema(description = "Autor do livro", example = "Robert C. Martin")
    private String autor;

    @NotBlank(message = "ISBN é obrigatório")
    @Pattern(regexp = "^[0-9-]{10,17}$", message = "ISBN inválido")
    @Schema(description = "ISBN do livro", example = "978-0-13-235088-4")
    private String isbn;

    @Schema(description = "Descrição do livro")
    private String descricao;

    @Schema(description = "Categoria do livro", example = "Tecnologia")
    private String categoria;

    @NotNull(message = "Cópias disponíveis é obrigatório")
    @Positive(message = "Cópias disponíveis deve ser positivo")
    @Schema(description = "Número de cópias disponíveis", example = "5")
    private Integer copiasDisponiveis;

    @NotNull(message = "Cópias totais é obrigatório")
    @Positive(message = "Cópias totais deve ser positivo")
    @Schema(description = "Número total de cópias", example = "10")
    private Integer copiasTotais;
}
