package com.biblioteca.service;

import com.biblioteca.dto.EmprestimoDTO;
import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.entity.Usuario;
import com.biblioteca.exception.ExcecaoNegocioException;
import com.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.repository.EmprestimoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço de negócio para operações com Empréstimos
 * 
 * Período padrão de empréstimo: 14 dias
 * Zona horária: America/Sao_Paulo (GMT-3/-5)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmprestimoService {

    private static final int DIAS_EMPRESTIMO_PADRAO = 14;

    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    /**
     * Cria um novo empréstimo
     */
    public Emprestimo criar(EmprestimoDTO dto) {
        log.info("Criando novo empréstimo para usuário ID: {} e livro ID: {}", 
                dto.getUsuarioId(), dto.getLivroId());

        // Valida e busca entidades
        Livro livro = livroService.buscarPorId(dto.getLivroId());
        Usuario usuario = usuarioService.buscarPorId(dto.getUsuarioId());

        // Validações de negócio
        validarEmprestimo(livro, usuario);

        LocalDate dataEmprestimo = dto.getDataEmprestimo() != null ? 
                dto.getDataEmprestimo() : LocalDate.now();

        Emprestimo emprestimo = Emprestimo.builder()
                .livro(livro)
                .usuario(usuario)
                .dataEmprestimo(dataEmprestimo)
                .dataDeVolucaoPrevista(dataEmprestimo.plusDays(DIAS_EMPRESTIMO_PADRAO))
                .status("ATIVO")
                .observacoes(dto.getObservacoes())
                .dataCriacao(LocalDateTime.now())
                .dataAtualizacao(LocalDateTime.now())
                .build();

        // Reduz cópias disponíveis do livro
        livroService.reservarCopia(livro.getId());

        Emprestimo novoEmprestimo = emprestimoRepository.save(emprestimo);
        log.info("Empréstimo criado com sucesso: ID {}", novoEmprestimo.getId());
        return novoEmprestimo;
    }

    /**
     * Devolve um livro emprestado
     */
    public Emprestimo devolver(Long id) {
        log.info("Processando devolução de empréstimo: {}", id);

        Emprestimo emprestimo = buscarPorId(id);

        if (!"ATIVO".equals(emprestimo.getStatus())) {
            throw new ExcecaoNegocioException("Empréstimo não está ativo");
        }

        emprestimo.setDataDeVolucaoReal(LocalDate.now());
        emprestimo.setStatus("DEVOLVIDO");
        emprestimo.setDataAtualizacao(LocalDateTime.now());

        // Aumenta cópias disponíveis do livro
        livroService.liberarCopia(emprestimo.getLivro().getId());

        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimo);
        log.info("Livro devolvido com sucesso: Empréstimo ID {}", emprestimoAtualizado.getId());
        return emprestimoAtualizado;
    }

    /**
     * Renova um empréstimo ativo
     */
    public Emprestimo renovar(Long id) {
        log.info("Renovando empréstimo: {}", id);

        Emprestimo emprestimo = buscarPorId(id);

        if (!"ATIVO".equals(emprestimo.getStatus())) {
            throw new ExcecaoNegocioException("Empréstimo não está ativo e não pode ser renovado");
        }

        if (emprestimo.estaAtrasado()) {
            throw new ExcecaoNegocioException("Empréstimo está atrasado e não pode ser renovado");
        }

        emprestimo.setDataDeVolucaoPrevista(emprestimo.getDataDeVolucaoPrevista().plusDays(DIAS_EMPRESTIMO_PADRAO));
        emprestimo.setDataAtualizacao(LocalDateTime.now());

        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimo);
        log.info("Empréstimo renovado com sucesso: ID {}", emprestimoAtualizado.getId());
        return emprestimoAtualizado;
    }

    /**
     * Busca um empréstimo por ID
     */
    @Transactional(readOnly = true)
    public Emprestimo buscarPorId(Long id) {
        log.debug("Buscando empréstimo com ID: {}", id);
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Empréstimo não encontrado com ID: {}", id);
                    return new RecursoNaoEncontradoException("Empréstimo não encontrado com ID: " + id);
                });
    }

    /**
     * Lista empréstimos ativos com paginação
     */
    @Transactional(readOnly = true)
    public Page<Emprestimo> listarAtivos(Pageable pageable) {
        log.debug("Listando empréstimos ativos");
        return emprestimoRepository.findByStatusOrderByDataEmprestimoDesc("ATIVO", pageable);
    }

    /**
     * Lista empréstimos de um usuário
     */
    @Transactional(readOnly = true)
    public Page<Emprestimo> listarPorUsuario(Long usuarioId, String status, Pageable pageable) {
        log.debug("Listando empréstimos do usuário ID: {} com status: {}", usuarioId, status);
        return emprestimoRepository.findByUsuarioIdAndStatusOrderByDataEmprestimoDesc(usuarioId, status, pageable);
    }

    /**
     * Lista empréstimos de um livro
     */
    @Transactional(readOnly = true)
    public Page<Emprestimo> listarPorLivro(Long livroId, Pageable pageable) {
        log.debug("Listando empréstimos do livro ID: {}", livroId);
        return emprestimoRepository.findByLivroIdOrderByDataEmprestimoDesc(livroId, pageable);
    }

    /**
     * Lista empréstimos atrasados
     */
    @Transactional(readOnly = true)
    public Page<Emprestimo> listarAtrasados(Pageable pageable) {
        log.debug("Listando empréstimos atrasados");
        return emprestimoRepository.buscarEmprestimosAtrasados(pageable);
    }

    /**
     * Lista empréstimos entre datas
     */
    @Transactional(readOnly = true)
    public Page<Emprestimo> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        log.debug("Listando empréstimos entre {} e {}", dataInicio, dataFim);
        return emprestimoRepository.findByDataEmprestimoEntre(dataInicio, dataFim, pageable);
    }

    /**
     * Obtém histórico de empréstimos de um usuário
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> obterHistoricoUsuario(Long usuarioId, LocalDate dataInicio, LocalDate dataFim) {
        log.debug("Obtendo histórico de empréstimos do usuário ID: {} entre {} e {}", 
                usuarioId, dataInicio, dataFim);
        return emprestimoRepository.findHistoricoUsuario(usuarioId, dataInicio, dataFim);
    }

    /**
     * Obtém empréstimos ativos de um usuário
     */
    @Transactional(readOnly = true)
    public List<Emprestimo> obterEmprestimosAtivosDoUsuario(Long usuarioId) {
        log.debug("Obtendo empréstimos ativos do usuário ID: {}", usuarioId);
        return emprestimoRepository.findEmprestimosAtivosDoUsuario(usuarioId);
    }

    /**
     * Deleta um empréstimo
     */
    public void deletar(Long id) {
        log.info("Deletando empréstimo com ID: {}", id);
        Emprestimo emprestimo = buscarPorId(id);
        emprestimoRepository.delete(emprestimo);
        log.info("Empréstimo deletado com sucesso: ID {}", id);
    }

    /**
     * Valida se um empréstimo pode ser realizado
     */
    private void validarEmprestimo(Livro livro, Usuario usuario) {
        // Valida se o usuário está ativo
        if (!usuario.getAtivo()) {
            throw new ExcecaoNegocioException("Usuário não está ativo");
        }

        // Valida se há cópias disponíveis
        if (livro.getCopiasDisponiveis() <= 0) {
            throw new ExcecaoNegocioException("Sem cópias disponíveis do livro");
        }

        // Valida limite de empréstimos do usuário
        Long emprestimosAtivos = emprestimoRepository.countByUsuarioIdAndStatus(usuario.getId(), "ATIVO");
        if (emprestimosAtivos >= usuario.getLimiteEmprestimos()) {
            throw new ExcecaoNegocioException("Usuário atingiu o limite de empréstimos simultâneos");
        }

        // Valida se o usuário tem empréstimos atrasados
        Page<Emprestimo> emprestimoAtrasado = emprestimoRepository.buscarEmprestimosAtrasados(
                org.springframework.data.domain.PageRequest.of(0, 1)
        );
        if (emprestimoAtrasado.hasContent()) {
            Emprestimo atrasado = emprestimoAtrasado.getContent().get(0);
            if (atrasado.getUsuario().getId().equals(usuario.getId())) {
                throw new ExcecaoNegocioException("Usuário possui empréstimo em atraso");
            }
        }
    }
}
