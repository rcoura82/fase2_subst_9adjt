package com.biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um Usuário no sistema de biblioteca.
 * Pode ser um aluno, professor ou membro da comunidade.
 * 
 * @author Biblioteca Online
 * @version 1.0
 */
@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    /**
     * Identificador único do usuário
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo do usuário (obrigatório)
     */
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    /**
     * Email do usuário (obrigatório e único)
     */
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;

    /**
     * Telefone de contato
     */
    @Column(name = "telefone", length = 20)
    private String telefone;

    /**
     * Endereço do usuário
     */
    @Column(name = "endereco", length = 500)
    private String endereco;

    /**
     * Tipo de usuário (ALUNO, PROFESSOR, VISITANTE, etc)
     */
    @Column(name = "tipo_usuario", nullable = false, length = 50)
    private String tipoUsuario;

    /**
     * Status ativo/inativo do usuário
     */
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    /**
     * Limite máximo de livros que o usuário pode emprestar simultaneamente
     */
    @Column(name = "limite_emprestimos")
    private Integer limiteEmprestimos = 5;

    /**
     * Data de criação do registro
     */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização
     */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Relacionamento com empréstimos
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Emprestimo> emprestimos = new HashSet<>();

    /**
     * Método chamado automaticamente antes de persistir a entidade
     */
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Método chamado automaticamente antes de atualizar a entidade
     */
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
