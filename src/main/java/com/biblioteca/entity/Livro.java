package com.biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa um Livro no sistema de biblioteca.
 * Contém informações sobre o livro e suas transações de empréstimo.
 * 
 * @author Biblioteca Online
 * @version 1.0
 */
@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Livro {

    /**
     * Identificador único do livro
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título do livro (obrigatório)
     */
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;

    /**
     * Autor do livro (obrigatório)
     */
    @Column(name = "autor", nullable = false, length = 255)
    private String autor;

    /**
     * ISBN do livro (International Standard Book Number) - único
     */
    @Column(name = "isbn", unique = true, nullable = false, length = 20)
    private String isbn;

    /**
     * Descrição detalhada do livro
     */
    @Column(name = "descricao", length = 1000)
    private String descricao;

    /**
     * Categoria ou gênero do livro
     */
    @Column(name = "categoria", length = 100)
    private String categoria;

    /**
     * Número total de cópias disponíveis do livro
     */
    @Column(name = "cópias_disponíveis", nullable = false)
    private Integer copiasDisponiveis;

    /**
     * Número total de cópias do livro na biblioteca
     */
    @Column(name = "cópias_totais", nullable = false)
    private Integer copiasTotais;

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
    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, orphanRemoval = true)
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

    /**
     * Calcula o número de cópias emprestadas
     * @return número de cópias emprestadas
     */
    public Integer getCopiasEmprestadas() {
        return copiasTotais - copiasDisponiveis;
    }
}
