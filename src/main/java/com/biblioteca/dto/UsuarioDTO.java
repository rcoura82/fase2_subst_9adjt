package com.biblioteca.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criar ou atualizar um Usuário
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    @Schema(description = "ID do usuário", example = "1")
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo do usuário", example = "João Silva")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email do usuário", example = "joao@example.com")
    private String email;

    @Schema(description = "Telefone do usuário", example = "(11) 98765-4321")
    private String telefone;

    @Schema(description = "Endereço do usuário")
    private String endereco;

    @NotBlank(message = "Tipo de usuário é obrigatório")
    @Schema(description = "Tipo de usuário", example = "ALUNO", allowableValues = {"ALUNO", "PROFESSOR", "VISITANTE", "FUNCIONARIO"})
    private String tipoUsuario;

    @Schema(description = "Status ativo/inativo", example = "true")
    private Boolean ativo = true;

    @Schema(description = "Limite de empréstimos simultâneos", example = "5")
    private Integer limiteEmprestimos = 5;
}
