package com.biblioteca.repository;

import com.biblioteca.entity.Emprestimo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositório JPA para operações de Empréstimo
 */
@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    /**
     * Busca empréstimos ativos de um usuário
     */
    Page<Emprestimo> findByUsuarioIdAndStatusOrderByDataEmprestimoDesc(Long usuarioId, String status, Pageable pageable);

    /**
     * Busca empréstimos de um livro
     */
    Page<Emprestimo> findByLivroIdOrderByDataEmprestimoDesc(Long livroId, Pageable pageable);

    /**
     * Busca empréstimos ativos
     */
    Page<Emprestimo> findByStatusOrderByDataEmprestimoDesc(String status, Pageable pageable);

    /**
     * Busca empréstimos atrasados
     */
    @Query("SELECT e FROM Emprestimo e WHERE e.status = 'ATIVO' AND e.dataDeVolucaoPrevista < CURRENT_DATE " +
            "ORDER BY e.dataDeVolucaoPrevista ASC")
    Page<Emprestimo> buscarEmprestimosAtrasados(Pageable pageable);

    /**
     * Busca empréstimos entre datas
     */
    @Query("SELECT e FROM Emprestimo e WHERE e.dataEmprestimo BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY e.dataEmprestimo DESC")
    Page<Emprestimo> findByDataEmprestimoEntre(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );

    /**
     * Busca empréstimos de um usuário entre datas
     */
    @Query("SELECT e FROM Emprestimo e WHERE e.usuario.id = :usuarioId " +
            "AND e.dataEmprestimo BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY e.dataEmprestimo DESC")
    List<Emprestimo> findHistoricoUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim
    );

    /**
     * Conta empréstimos ativos de um usuário
     */
    Long countByUsuarioIdAndStatus(Long usuarioId, String status);

    /**
     * Busca empréstimos ativos que não foram devolvidos
     */
    @Query("SELECT e FROM Emprestimo e WHERE e.status = 'ATIVO' AND e.usuario.id = :usuarioId")
    List<Emprestimo> findEmprestimosAtivosDoUsuario(@Param("usuarioId") Long usuarioId);
}
