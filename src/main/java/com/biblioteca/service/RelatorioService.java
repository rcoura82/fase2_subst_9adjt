package com.biblioteca.service;

import com.biblioteca.entity.Emprestimo;
import com.biblioteca.entity.Livro;
import com.biblioteca.repository.EmprestimoRepository;
import com.biblioteca.repository.LivroRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço para gerar relatórios da biblioteca
 * Utiliza Java Streams para processamento eficiente de coleções
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RelatorioService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    /**
     * Gera relatório dos 20 livros mais emprestados
     * Utiliza Stream para filtrar e ordenar
     */
    public List<LivroComContagemDTO> relatorio20LivrosMaisEmprestados() {
        log.info("Gerando relatório dos 20 livros mais emprestados");

        List<Livro> livrosMaisEmprestados = livroRepository.findTop20MaisEmprestados();

        return livrosMaisEmprestados.stream()
                .map(livro -> {
                    long contagemEmprestimos = livro.getEmprestimos().stream()
                            .filter(emp -> "ATIVO".equals(emp.getStatus()) || "DEVOLVIDO".equals(emp.getStatus()))
                            .count();
                    
                    return LivroComContagemDTO.builder()
                            .id(livro.getId())
                            .titulo(livro.getTitulo())
                            .autor(livro.getAutor())
                            .isbn(livro.getIsbn())
                            .categoria(livro.getCategoria())
                            .copiasDisponiveis(livro.getCopiasDisponiveis())
                            .copiasTotais(livro.getCopiasTotais())
                            .quantidadeEmprestimos(contagemEmprestimos)
                            .build();
                })
                .limit(20)
                .collect(Collectors.toList());
    }

    /**
     * Gera relatório de livros emprestados com previsão de devolução
     * Agrupa por livro e mostra próximos a vencer
     */
    public List<LivroEmprestadoDTO> relatorioLivrosEmprestados() {
        log.info("Gerando relatório de livros emprestados");

        return emprestimoRepository.findByStatusOrderByDataEmprestimoDesc("ATIVO", 
                org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE))
                .stream()
                .map(emprestimo -> LivroEmprestadoDTO.builder()
                        .emprestimoId(emprestimo.getId())
                        .livroId(emprestimo.getLivro().getId())
                        .livroTitulo(emprestimo.getLivro().getTitulo())
                        .livroAutor(emprestimo.getLivro().getAutor())
                        .livroIsbn(emprestimo.getLivro().getIsbn())
                        .usuarioId(emprestimo.getUsuario().getId())
                        .usuarioNome(emprestimo.getUsuario().getNome())
                        .usuarioEmail(emprestimo.getUsuario().getEmail())
                        .dataEmprestimo(emprestimo.getDataEmprestimo())
                        .dataDeVolucaoPrevista(emprestimo.getDataDeVolucaoPrevista())
                        .diasRestantes(java.time.temporal.ChronoUnit.DAYS.between(
                                LocalDate.now(), 
                                emprestimo.getDataDeVolucaoPrevista()
                        ))
                        .estaAtrasado(emprestimo.estaAtrasado())
                        .diasAtrasados(emprestimo.calcularDiasAtrasados())
                        .build())
                .sorted(Comparator.comparing(LivroEmprestadoDTO::getDataDeVolucaoPrevista))
                .collect(Collectors.toList());
    }

    /**
     * Gera relatório de empréstimos por usuário
     */
    public List<UsuarioEmprestimosDTO> relatorioEmprestimosPorUsuario() {
        log.info("Gerando relatório de empréstimos por usuário");

        return emprestimoRepository.findAll().stream()
                .collect(Collectors.groupingBy(emp -> emp.getUsuario()))
                .entrySet()
                .stream()
                .map(entry -> UsuarioEmprestimosDTO.builder()
                        .usuarioId(entry.getKey().getId())
                        .usuarioNome(entry.getKey().getNome())
                        .usuarioEmail(entry.getKey().getEmail())
                        .usuarioTipo(entry.getKey().getTipoUsuario())
                        .totalEmprestimos(entry.getValue().size())
                        .emprestimosDevolvidosCount(entry.getValue().stream()
                                .filter(emp -> "DEVOLVIDO".equals(emp.getStatus()))
                                .count())
                        .emprestimosAtivosCount(entry.getValue().stream()
                                .filter(emp -> "ATIVO".equals(emp.getStatus()))
                                .count())
                        .emprestimosAtrasadosCount(entry.getValue().stream()
                                .filter(Emprestimo::estaAtrasado)
                                .count())
                        .build())
                .sorted(Comparator.comparingLong(UsuarioEmprestimosDTO::getTotalEmprestimos).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Gera relatório de livros por categoria
     */
    public List<LivroPorCategoriaDTO> relatorioLivrosPorCategoria() {
        log.info("Gerando relatório de livros por categoria");

        return livroRepository.findAll().stream()
                .collect(Collectors.groupingBy(Livro::getCategoria))
                .entrySet()
                .stream()
                .map(entry -> LivroPorCategoriaDTO.builder()
                        .categoria(entry.getKey() != null ? entry.getKey() : "Sem Categoria")
                        .totalLivros(entry.getValue().size())
                        .copiasDisponiveis(entry.getValue().stream()
                                .mapToInt(Livro::getCopiasDisponiveis)
                                .sum())
                        .copiasTotais(entry.getValue().stream()
                                .mapToInt(Livro::getCopiasTotais)
                                .sum())
                        .taxaDisponibilidade(calcularTaxaDisponibilidade(entry.getValue()))
                        .livros(entry.getValue().stream()
                                .map(l -> new LivroResumoDTO(l.getId(), l.getTitulo(), l.getAutor(), l.getIsbn()))
                                .collect(Collectors.toList()))
                        .build())
                .sorted(Comparator.comparing(LivroPorCategoriaDTO::getCategoria))
                .collect(Collectors.toList());
    }

    /**
     * Gera relatório de atividade por período
     */
    public RelatorioAtividadeDTO relatorioAtividadePeriodo(LocalDate dataInicio, LocalDate dataFim) {
        log.info("Gerando relatório de atividade de {} a {}", dataInicio, dataFim);

        List<Emprestimo> emprestimosPeriodo = emprestimoRepository.findByDataEmprestimoEntre(
                dataInicio, dataFim, 
                org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE)
        ).toList();

        long devolucoesAtraso = emprestimosPeriodo.stream()
                .filter(Emprestimo::estaAtrasado)
                .count();

        long totalRenovacoes = emprestimosPeriodo.stream()
                .filter(emp -> emp.getDataDeVolucaoPrevista().plusDays(-14).isBefore(emp.getDataEmprestimo()))
                .count();

        return RelatorioAtividadeDTO.builder()
                .dataInicio(dataInicio)
                .dataFim(dataFim)
                .totalEmprestimos(emprestimosPeriodo.size())
                .totalDevolucoes(emprestimosPeriodo.stream()
                        .filter(emp -> "DEVOLVIDO".equals(emp.getStatus()))
                        .count())
                .emprestimoAtrasados(devolucoesAtraso)
                .taxaAtraso(emprestimosPeriodo.isEmpty() ? 0 : 
                        (devolucoesAtraso * 100.0) / emprestimosPeriodo.size())
                .build();
    }

    /**
     * Calcula a taxa de disponibilidade de um grupo de livros
     */
    private Double calcularTaxaDisponibilidade(List<Livro> livros) {
        if (livros.isEmpty()) {
            return 0.0;
        }
        
        long totalCopias = livros.stream()
                .mapToLong(Livro::getCopiasTotais)
                .sum();
        
        long copiasDisponiveis = livros.stream()
                .mapToLong(Livro::getCopiasDisponiveis)
                .sum();
        
        return totalCopias == 0 ? 0.0 : (copiasDisponiveis * 100.0) / totalCopias;
    }

    /**
     * DTO para livro com contagem de empréstimos
     */
    @lombok.Data
    @lombok.Builder
    public static class LivroComContagemDTO {
        private Long id;
        private String titulo;
        private String autor;
        private String isbn;
        private String categoria;
        private Integer copiasDisponiveis;
        private Integer copiasTotais;
        private long quantidadeEmprestimos;
    }

    /**
     * DTO para livro emprestado
     */
    @lombok.Data
    @lombok.Builder
    public static class LivroEmprestadoDTO {
        private Long emprestimoId;
        private Long livroId;
        private String livroTitulo;
        private String livroAutor;
        private String livroIsbn;
        private Long usuarioId;
        private String usuarioNome;
        private String usuarioEmail;
        private LocalDate dataEmprestimo;
        private LocalDate dataDeVolucaoPrevista;
        private long diasRestantes;
        private Boolean estaAtrasado;
        private long diasAtrasados;
    }

    /**
     * DTO para resumo de empréstimos por usuário
     */
    @lombok.Data
    @lombok.Builder
    public static class UsuarioEmprestimosDTO {
        private Long usuarioId;
        private String usuarioNome;
        private String usuarioEmail;
        private String usuarioTipo;
        private long totalEmprestimos;
        private long emprestimosDevolvidosCount;
        private long emprestimosAtivosCount;
        private long emprestimosAtrasadosCount;
    }

    /**
     * DTO para livros por categoria
     */
    @lombok.Data
    @lombok.Builder
    public static class LivroPorCategoriaDTO {
        private String categoria;
        private int totalLivros;
        private int copiasDisponiveis;
        private int copiasTotais;
        private Double taxaDisponibilidade;
        private List<LivroResumoDTO> livros;
    }

    /**
     * DTO para resumo de livro
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class LivroResumoDTO {
        private Long id;
        private String titulo;
        private String autor;
        private String isbn;
    }

    /**
     * DTO para relatório de atividade
     */
    @lombok.Data
    @lombok.Builder
    public static class RelatorioAtividadeDTO {
        private LocalDate dataInicio;
        private LocalDate dataFim;
        private long totalEmprestimos;
        private long totalDevolucoes;
        private long emprestimoAtrasados;
        private Double taxaAtraso;
    }
}
