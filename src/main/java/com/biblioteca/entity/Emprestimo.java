package com.biblioteca.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade que representa um Empréstimo de livro.
 * Rastreia qual livro foi emprestado, para qual usuário, e as datas de empréstimo/devolução.
 * 
 * Zona horária padrão do sistema: America/Sao_Paulo (GMT-3/-5)
 * 
 * @author Biblioteca Online
 * @version 1.0
 */
@Entity
@Table(name = "emprestimos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emprestimo {

    /**
     * Identificador único do empréstimo
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento com Livro emprestado
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    /**
     * Relacionamento com Usuário que pegou o livro
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Data de empréstimo (obrigatória)
     * Formato: LocalDate (YYYY-MM-DD)
     */
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    /**
     * Data prevista de devolução (obrigatória)
     * Calculada como: dataEmprestimo + 14 dias (padrão)
     */
    @Column(name = "data_devolucao_prevista", nullable = false)
    private LocalDate dataDeVolucaoPrevista;

    /**
     * Data real de devolução (null enquanto livro estiver emprestado)
     */
    @Column(name = "data_devolucao_real")
    private LocalDate dataDeVolucaoReal;

    /**
     * Status do empréstimo (ATIVO, DEVOLVIDO, ATRASADO, CANCELADO)
     */
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    /**
     * Observações adicionais sobre o empréstimo
     */
    @Column(name = "observacoes", length = 500)
    private String observacoes;

    /**
     * Data de criação do registro de empréstimo
     */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização do registro
     */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

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
     * Verifica se o empréstimo está em atraso
     * @return true se a data de devolução prevista já passou e o livro não foi devolvido
     */
    public boolean estaAtrasado() {
        return "ATIVO".equals(status) && LocalDate.now().isAfter(dataDeVolucaoPrevista);
    }

    /**
     * Calcula dias em atraso
     * @return número de dias em atraso, 0 se não está atrasado
     */
    public long calcularDiasAtrasados() {
        if (estaAtrasado()) {
            return java.time.temporal.ChronoUnit.DAYS.between(dataDeVolucaoPrevista, LocalDate.now());
        }
        return 0;
    }
}
