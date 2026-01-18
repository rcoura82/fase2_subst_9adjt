# 📋 COMO GERAR O PDF DO RELATÓRIO TÉCNICO

O projeto inclui scripts automatizados para gerar o PDF com toda a documentação técnica.

## ✅ Opção 1: Usando Pandoc (Recomendado)

### Pré-requisitos
- `pandoc` instalado
- `pdflatex` (LaTeX)

### Executar Script
```bash
cd /workspaces/fase2_subst_9adjt
chmod +x gerar-pdf.sh
./gerar-pdf.sh
```

**O script irá:**
1. ✓ Verificar se pandoc está instalado
2. ✓ Instalar se necessário
3. ✓ Converter PROJETO_COMPLETO.md para PDF
4. ✓ Fazer commit e push do PDF

**Resultado:**
- Arquivo: `SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf`
- Tamanho: ~5-10 MB
- Qualidade: Alta (300 dpi)

---

## 🔄 Opção 2: Usando wkhtmltopdf (Alternativa)

Se pandoc não funcionar, use:

```bash
cd /workspaces/fase2_subst_9adjt
chmod +x gerar-pdf-alternativo.sh
./gerar-pdf-alternativo.sh
```

---

## 🖥️ Opção 3: Manual via Pandoc

Se preferir rodar manualmente:

```bash
cd /workspaces/fase2_subst_9adjt

# Instalar pandoc (Ubuntu/Debian)
sudo apt-get update
sudo apt-get install -y pandoc pandoc-latex-environment texlive-latex-base texlive-fonts-recommended

# Converter para PDF
pandoc PROJETO_COMPLETO.md \
    -o SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf \
    --pdf-engine=pdflatex \
    --toc \
    --toc-depth=3 \
    -V colorlinks=true \
    -V linkcolor=blue

# Fazer commit
git add SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf
git commit -m "docs: Add final technical report PDF"
git push origin main
```

---

## 📄 O QUE ESTÁ NO PDF

O arquivo PDF gerado contém:

### 1. Link do Repositório GitHub
```
https://github.com/rcoura82/fase2_subst_9adjt
```

### 2. Documentação Técnica da API
- ✓ 33+ endpoints REST detalhados
- ✓ Exemplos de requisição e resposta
- ✓ Parâmetros e validações
- ✓ Códigos HTTP
- ✓ DTOs com campos

**Endpoints inclusos:**
- Livros (8 endpoints)
- Usuários (11 endpoints)
- Empréstimos (12 endpoints)
- Relatórios (5 endpoints)

### 3. Relatório Técnico Completo

#### 3.1 Tecnologias e Ferramentas
- Java 21 LTS
- Spring Boot 3.2.0
- PostgreSQL 15
- Docker & Docker Compose
- Swagger/OpenAPI 3.0

#### 3.2 Arquitetura e Design Patterns
- Padrão MVC
- Separação em camadas
- Repository pattern
- Service pattern
- DTO pattern

#### 3.3 Desafios e Soluções
1. **Gerenciamento de Datas e Timezones**
   - Solução: LocalDate + timezone centralizado

2. **Problema N+1 Query**
   - Solução: JOIN FETCH em queries

3. **Validação Complexa**
   - Solução: Service centralizado

4. **Escalabilidade com Docker**
   - Solução: Multi-stage build (200MB vs 800MB)

5. **Orquestração de Containers**
   - Solução: Health checks + depends_on

6. **Performance de Relatórios**
   - Solução: Java Streams + queries otimizadas

### 4. Estatísticas do Projeto
- 20+ classes Java
- ~3.500 linhas de código
- 33+ endpoints REST
- 19 custom queries
- 12.000+ linhas de documentação

### 5. Deployment e Escalabilidade
- Docker build process
- Docker Compose setup
- Escalabilidade horizontal
- Load balancing
- Persistência de dados

### 6. Monitoramento e Logs
- Health checks
- Logging configuration
- Metrics

### 7. Segurança
- Boas práticas implementadas
- Recomendações futuras

### 8. Testes
- Estrutura de testes
- Como executar

### 9. Desenvolvimento Local
- Pré-requisitos
- Setup inicial

### 10. Conclusão
- Resumo das melhores práticas
- Status do projeto

---

## 📊 Especificações do PDF

| Aspecto | Detalhes |
|---------|----------|
| **Nome do Arquivo** | SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf |
| **Formato** | PDF (A4, 11pt) |
| **Páginas** | ~20-30 (conforme conteúdo) |
| **Tamanho** | ~5-10 MB |
| **DPI** | 300 (alta qualidade) |
| **Índice** | Sim (Table of Contents) |
| **Links Coloridos** | Sim (azul) |
| **Margens** | 1 polegada em todos os lados |

---

## ✨ Características do PDF

✅ **Índice Automático** (Table of Contents)  
✅ **Links Clicáveis** (GitHub, URLs)  
✅ **Formatação Profissional** (Headers, código, tabelas)  
✅ **Código com Sintaxe** (Monospace font)  
✅ **Tabelas Formatadas** (Bordas, cores)  
✅ **Paginação** (Numeração automática)  
✅ **Quebras de Página** (Entre seções)  

---

## 🔗 Arquivos Relacionados

| Arquivo | Descrição |
|---------|-----------|
| **PROJETO_COMPLETO.md** | Markdown fonte (convertido para PDF) |
| **RELATORIO_TECNICO.md** | Relatório técnico expandido |
| **EXEMPLOS_REQUISICOES.md** | Exemplos HTTP detalhados |
| **README.md** | Visão geral do projeto |
| **INSTALACAO.md** | Guia de instalação |
| **GUIA_RAPIDO.md** | Quick start guide |

---

## 🚀 Após Gerar o PDF

1. **Verificar o arquivo**
   ```bash
   ls -lh SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf
   ```

2. **Abrir o PDF**
   ```bash
   # Linux
   xdg-open SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf
   
   # macOS
   open SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf
   ```

3. **Fazer commit (automático via script)**
   ```bash
   git log --oneline | head -1
   ```

4. **Verificar no GitHub**
   ```
   https://github.com/rcoura82/fase2_subst_9adjt/blob/main/SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf
   ```

---

## 🆘 Troubleshooting

### Pandoc não encontrado
```bash
sudo apt-get install -y pandoc
```

### LaTeX não encontrado
```bash
sudo apt-get install -y texlive-latex-base texlive-fonts-recommended
```

### Permissão negada
```bash
chmod +x gerar-pdf.sh
```

### Git push falha
```bash
git config user.email "seu-email@github.com"
git config user.name "Seu Nome"
git push origin main
```

---

## 📝 Estrutura Markdown para PDF

O arquivo `PROJETO_COMPLETO.md` usa:

```markdown
# Título Principal (h1)
## Subtítulo (h2)
### Seção (h3)

**Negrito**
*Itálico*
`código`

```código em bloco```

| Coluna 1 | Coluna 2 |
|----------|----------|
| Célula 1 | Célula 2 |

- Lista item 1
- Lista item 2

1. Item numerado
2. Segundo item
```

---

## 🎯 Resultado Final

Após executar o script ou comandos manuais, você terá:

✅ **PDF Gerado**: `SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf`  
✅ **Commitado**: No repositório Git  
✅ **Publicado**: No GitHub (visível em web)  
✅ **Pronto para**: Apresentação, entrega, arquivo  

---

**Tempo estimado**: 2-5 minutos  
**Dependências**: pandoc, pdflatex  
**Saída**: PDF profissional com 20-30 páginas  

🎉 **Pronto para usar!**
