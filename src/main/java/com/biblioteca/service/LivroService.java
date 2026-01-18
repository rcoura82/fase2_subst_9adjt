package com.biblioteca.service;

import com.biblioteca.dto.LivroDTO;
import com.biblioteca.entity.Livro;
import com.biblioteca.exception.ExcecaoNegocioException;
import com.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço de negócio para operações com Livros
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class LivroService {

    private final LivroRepository livroRepository;

    /**
     * Cria um novo livro
     */
    public Livro criar(LivroDTO dto) {
        log.info("Criando novo livro com ISBN: {}", dto.getIsbn());

        // Valida se ISBN já existe
        if (livroRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new ExcecaoNegocioException("Livro com ISBN " + dto.getIsbn() + " já existe");
        }

        // Valida se cópias disponíveis não excede cópias totais
        if (dto.getCopiasDisponiveis() > dto.getCopiasTotais()) {
            throw new ExcecaoNegocioException("Cópias disponíveis não pode ser maior que cópias totais");
        }

        Livro livro = Livro.builder()
                .titulo(dto.getTitulo())
                .autor(dto.getAutor())
                .isbn(dto.getIsbn())
                .descricao(dto.getDescricao())
                .categoria(dto.getCategoria())
                .copiasDisponiveis(dto.getCopiasDisponiveis())
                .copiasTotais(dto.getCopiasTotais())
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        Livro novoLivro = livroRepository.save(livro);
        log.info("Livro criado com sucesso: ID {}", novoLivro.getId());
        return novoLivro;
    }

    /**
     * Atualiza um livro existente
     */
    public Livro atualizar(Long id, LivroDTO dto) {
        log.info("Atualizando livro com ID: {}", id);

        Livro livro = buscarPorId(id);

        // Valida ISBN se foi alterado
        if (!livro.getIsbn().equals(dto.getIsbn()) && 
            livroRepository.findByIsbn(dto.getIsbn()).isPresent()) {
            throw new ExcecaoNegocioException("Livro com ISBN " + dto.getIsbn() + " já existe");
        }

        // Valida cópias
        if (dto.getCopiasDisponiveis() > dto.getCopiasTotais()) {
            throw new ExcecaoNegocioException("Cópias disponíveis não pode ser maior que cópias totais");
        }

        livro.setTitulo(dto.getTitulo());
        livro.setAutor(dto.getAutor());
        livro.setIsbn(dto.getIsbn());
        livro.setDescricao(dto.getDescricao());
        livro.setCategoria(dto.getCategoria());
        livro.setCopiasDisponiveis(dto.getCopiasDisponiveis());
        livro.setCopiasTotais(dto.getCopiasTotais());
        livro.setDataAtualizacao(LocalDateTime.now());

        Livro livroAtualizado = livroRepository.save(livro);
        log.info("Livro atualizado com sucesso: ID {}", livroAtualizado.getId());
        return livroAtualizado;
    }

    /**
     * Busca um livro por ID
     */
    @Transactional(readOnly = true)
    public Livro buscarPorId(Long id) {
        log.debug("Buscando livro com ID: {}", id);
        return livroRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Livro não encontrado com ID: {}", id);
                    return new RecursoNaoEncontradoException("Livro não encontrado com ID: " + id);
                });
    }

    /**
     * Busca livro por ISBN
     */
    @Transactional(readOnly = true)
    public Livro buscarPorIsbn(String isbn) {
        log.debug("Buscando livro com ISBN: {}", isbn);
        return livroRepository.findByIsbn(isbn)
                .orElseThrow(() -> {
                    log.error("Livro não encontrado com ISBN: {}", isbn);
                    return new RecursoNaoEncontradoException("Livro não encontrado com ISBN: " + isbn);
                });
    }

    /**
     * Busca livros com filtros
     */
    @Transactional(readOnly = true)
    public Page<Livro> buscarComFiltros(String titulo, String autor, String isbn, String categoria, Pageable pageable) {
        log.debug("Buscando livros com filtros - Título: {}, Autor: {}, ISBN: {}, Categoria: {}", 
                titulo, autor, isbn, categoria);
        return livroRepository.buscarComFiltros(titulo, autor, isbn, categoria, pageable);
    }

    /**
     * Busca livros disponíveis
     */
    @Transactional(readOnly = true)
    public Page<Livro> buscarDisponiveis(Pageable pageable) {
        log.debug("Buscando livros disponíveis");
        return livroRepository.findByCopiasDisponiveisGreaterThan(0, pageable);
    }

    /**
     * Retorna os 20 livros mais emprestados
     */
    @Transactional(readOnly = true)
    public List<Livro> obterTop20MaisEmprestados() {
        log.debug("Obtendo top 20 livros mais emprestados");
        return livroRepository.findTop20MaisEmprestados();
    }

    /**
     * Lista todos os livros com paginação
     */
    @Transactional(readOnly = true)
    public Page<Livro> listarTodos(Pageable pageable) {
        log.debug("Listando todos os livros");
        return livroRepository.findAll(pageable);
    }

    /**
     * Deleta um livro
     */
    public void deletar(Long id) {
        log.info("Deletando livro com ID: {}", id);
        Livro livro = buscarPorId(id);

        if (!livro.getEmprestimos().isEmpty()) {
            throw new ExcecaoNegocioException("Não é possível deletar livro com empréstimos pendentes");
        }

        livroRepository.delete(livro);
        log.info("Livro deletado com sucesso: ID {}", id);
    }

    /**
     * Reserva uma cópia de um livro (reduz quantidade disponível)
     */
    public void reservarCopia(Long id) {
        log.info("Reservando cópia de livro com ID: {}", id);
        Livro livro = buscarPorId(id);

        if (livro.getCopiasDisponiveis() <= 0) {
            throw new ExcecaoNegocioException("Sem cópias disponíveis do livro");
        }

        livro.setCopiasDisponiveis(livro.getCopiasDisponiveis() - 1);
        livroRepository.save(livro);
        log.info("Cópia reservada com sucesso");
    }

    /**
     * Libera uma cópia de um livro (aumenta quantidade disponível)
     */
    public void liberarCopia(Long id) {
        log.info("Liberando cópia de livro com ID: {}", id);
        Livro livro = buscarPorId(id);

        if (livro.getCopiasDisponiveis() >= livro.getCopiasTotais()) {
            throw new ExcecaoNegocioException("Todas as cópias já estão disponíveis");
        }

        livro.setCopiasDisponiveis(livro.getCopiasDisponiveis() + 1);
        livroRepository.save(livro);
        log.info("Cópia liberada com sucesso");
    }
}
