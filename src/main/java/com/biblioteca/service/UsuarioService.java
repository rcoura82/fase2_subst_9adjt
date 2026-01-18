package com.biblioteca.service;

import com.biblioteca.dto.UsuarioDTO;
import com.biblioteca.entity.Usuario;
import com.biblioteca.exception.ExcecaoNegocioException;
import com.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Serviço de negócio para operações com Usuários
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Cria um novo usuário
     */
    public Usuario criar(UsuarioDTO dto) {
        log.info("Criando novo usuário com email: {}", dto.getEmail());

        // Valida se email já existe
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ExcecaoNegocioException("Usuário com email " + dto.getEmail() + " já existe");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .telefone(dto.getTelefone())
                .endereco(dto.getEndereco())
                .tipoUsuario(dto.getTipoUsuario())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .limiteEmprestimos(dto.getLimiteEmprestimos() != null ? dto.getLimiteEmprestimos() : 5)
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        Usuario novoUsuario = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso: ID {}", novoUsuario.getId());
        return novoUsuario;
    }

    /**
     * Atualiza um usuário existente
     */
    public Usuario atualizar(Long id, UsuarioDTO dto) {
        log.info("Atualizando usuário com ID: {}", id);

        Usuario usuario = buscarPorId(id);

        // Valida email se foi alterado
        if (!usuario.getEmail().equals(dto.getEmail()) && 
            usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ExcecaoNegocioException("Usuário com email " + dto.getEmail() + " já existe");
        }

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setEndereco(dto.getEndereco());
        usuario.setTipoUsuario(dto.getTipoUsuario());
        usuario.setAtivo(dto.getAtivo());
        usuario.setLimiteEmprestimos(dto.getLimiteEmprestimos());
        usuario.setDataAtualizacao(LocalDateTime.now());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        log.info("Usuário atualizado com sucesso: ID {}", usuarioAtualizado.getId());
        return usuarioAtualizado;
    }

    /**
     * Busca um usuário por ID
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        log.debug("Buscando usuário com ID: {}", id);
        return usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com ID: {}", id);
                    return new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id);
                });
    }

    /**
     * Busca usuário por email
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        log.debug("Buscando usuário com email: {}", email);
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Usuário não encontrado com email: {}", email);
                    return new RecursoNaoEncontradoException("Usuário não encontrado com email: " + email);
                });
    }

    /**
     * Lista todos os usuários com paginação
     */
    @Transactional(readOnly = true)
    public Page<Usuario> listarTodos(Pageable pageable) {
        log.debug("Listando todos os usuários");
        return usuarioRepository.findAll(pageable);
    }

    /**
     * Busca usuários por nome
     */
    @Transactional(readOnly = true)
    public Page<Usuario> buscarPorNome(String nome, Pageable pageable) {
        log.debug("Buscando usuários por nome: {}", nome);
        return usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    /**
     * Busca usuários por tipo
     */
    @Transactional(readOnly = true)
    public Page<Usuario> buscarPorTipo(String tipo, Pageable pageable) {
        log.debug("Buscando usuários por tipo: {}", tipo);
        return usuarioRepository.findByTipoUsuario(tipo, pageable);
    }

    /**
     * Lista usuários ativos
     */
    @Transactional(readOnly = true)
    public Page<Usuario> listarAtivos(Pageable pageable) {
        log.debug("Listando usuários ativos");
        return usuarioRepository.findByAtivoTrue(pageable);
    }

    /**
     * Deleta um usuário
     */
    public void deletar(Long id) {
        log.info("Deletando usuário com ID: {}", id);
        Usuario usuario = buscarPorId(id);

        if (!usuario.getEmprestimos().isEmpty()) {
            throw new ExcecaoNegocioException("Não é possível deletar usuário com empréstimos pendentes");
        }

        usuarioRepository.delete(usuario);
        log.info("Usuário deletado com sucesso: ID {}", id);
    }

    /**
     * Ativa um usuário
     */
    public Usuario ativar(Long id) {
        log.info("Ativando usuário com ID: {}", id);
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    /**
     * Desativa um usuário
     */
    public Usuario desativar(Long id) {
        log.info("Desativando usuário com ID: {}", id);
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        return usuarioRepository.save(usuario);
    }
}
