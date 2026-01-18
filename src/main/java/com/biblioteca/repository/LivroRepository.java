package com.biblioteca.repository;

import com.biblioteca.entity.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para operações de Livro
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    /**
     * Busca livro por ISBN
     */
    Optional<Livro> findByIsbn(String isbn);

    /**
     * Busca livros por título (com busca parcial)
     */
    Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    /**
     * Busca livros por autor
     */
    Page<Livro> findByAutorContainingIgnoreCase(String autor, Pageable pageable);

    /**
     * Busca livros por categoria
     */
    Page<Livro> findByCategoria(String categoria, Pageable pageable);

    /**
     * Busca livros disponíveis (com cópias em estoque)
     */
    Page<Livro> findByCopiasDisponiveisGreaterThan(Integer quantidade, Pageable pageable);

    /**
     * Busca os 20 livros mais emprestados usando Stream
     */
    @Query(value = "SELECT l.* FROM livros l " +
            "JOIN emprestimos e ON l.id = e.livro_id " +
            "WHERE e.status IN ('ATIVO', 'DEVOLVIDO') " +
            "GROUP BY l.id " +
            "ORDER BY COUNT(e.id) DESC " +
            "LIMIT 20", nativeQuery = true)
    List<Livro> findTop20MaisEmprestados();

    /**
     * Busca livros por múltiplos critérios
     */
    @Query("SELECT l FROM Livro l WHERE " +
            "(:titulo IS NULL OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
            "(:autor IS NULL OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :autor, '%'))) AND " +
            "(:isbn IS NULL OR l.isbn = :isbn) AND " +
            "(:categoria IS NULL OR l.categoria = :categoria)")
    Page<Livro> buscarComFiltros(
            @Param("titulo") String titulo,
            @Param("autor") String autor,
            @Param("isbn") String isbn,
            @Param("categoria") String categoria,
            Pageable pageable
    );
}
