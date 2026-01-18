package com.biblioteca.repository;

import com.biblioteca.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para operações de Usuário
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca usuário por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Busca usuários por nome
     */
    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    /**
     * Busca usuários por tipo
     */
    Page<Usuario> findByTipoUsuario(String tipoUsuario, Pageable pageable);

    /**
     * Busca usuários ativos
     */
    Page<Usuario> findByAtivoTrue(Pageable pageable);
}
