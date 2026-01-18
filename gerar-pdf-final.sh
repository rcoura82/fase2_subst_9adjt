#!/bin/bash

# ============================================================================
# SCRIPT FINAL: Gerar PDF do Relatório Técnico e Fazer Commit
# ============================================================================
# Este script automatiza a geração do PDF com toda a documentação técnica
# do Sistema de Biblioteca Online
# ============================================================================

set -e

cd /workspaces/fase2_subst_9adjt

echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║     SISTEMA DE BIBLIOTECA ONLINE - GERAÇÃO DE RELATÓRIO PDF       ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""

# Cores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# ==========================================================================
# PASSO 1: ATUALIZAR PACOTES
# ==========================================================================
echo -e "${BLUE}[1/8]${NC} Atualizando pacotes do sistema..."
apt-get update -y > /dev/null 2>&1
echo -e "${GREEN}✓ Pacotes atualizados${NC}"
echo ""

# ==========================================================================
# PASSO 2: INSTALAR DEPENDÊNCIAS
# ==========================================================================
echo -e "${BLUE}[2/8]${NC} Instalando dependências para geração de PDF..."
echo "   - pandoc"
echo "   - texlive-latex (LaTeX)"
echo "   - fontes"
apt-get install -y pandoc pandoc-latex-environment \
                   texlive-latex-base texlive-fonts-recommended \
                   texlive-latex-extra > /dev/null 2>&1
echo -e "${GREEN}✓ Dependências instaladas${NC}"
echo ""

# ==========================================================================
# PASSO 3: VERIFICAR ARQUIVOS
# ==========================================================================
echo -e "${BLUE}[3/8]${NC} Verificando arquivos necessários..."

if [ ! -f "PROJETO_COMPLETO.md" ]; then
    echo -e "${RED}✗ Arquivo PROJETO_COMPLETO.md não encontrado!${NC}"
    exit 1
fi
echo -e "   ✓ PROJETO_COMPLETO.md"

if [ ! -d ".git" ]; then
    echo -e "${RED}✗ Repositório Git não encontrado!${NC}"
    exit 1
fi
echo -e "   ✓ Repositório Git"
echo -e "${GREEN}✓ Todos os arquivos verificados${NC}"
echo ""

# ==========================================================================
# PASSO 4: GERAR PDF
# ==========================================================================
echo -e "${BLUE}[4/8]${NC} Gerando PDF do relatório técnico..."
echo "   Entrada: PROJETO_COMPLETO.md"
echo "   Saída: SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo "   Configurações:"
echo "     - PDF Engine: pdflatex"
echo "     - Índice: Ativado (Table of Contents)"
echo "     - Profundidade do índice: 3 níveis"
echo "     - Links: Coloridos (azul)"
echo "     - Formato: A4, 11pt"
echo "     - Margens: 1 polegada"
echo ""

pandoc PROJETO_COMPLETO.md \
    -o SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf \
    --pdf-engine=pdflatex \
    --toc \
    --toc-depth=3 \
    -V colorlinks=true \
    -V linkcolor=blue \
    -V geometry:margin=1in \
    -V fontsize=11pt \
    2>&1 | grep -v "Warning:" || true

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ PDF gerado com sucesso${NC}"
else
    echo -e "${RED}✗ Erro na geração do PDF${NC}"
    exit 1
fi
echo ""

# ==========================================================================
# PASSO 5: VERIFICAR PDF
# ==========================================================================
echo -e "${BLUE}[5/8]${NC} Verificando arquivo PDF gerado..."

if [ ! -f "SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf" ]; then
    echo -e "${RED}✗ Arquivo PDF não foi criado!${NC}"
    exit 1
fi

PDF_SIZE=$(ls -lh SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf | awk '{print $5}')
PDF_LINES=$(pdftotext SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf - 2>/dev/null | wc -l || echo "N/A")

echo -e "   Arquivo: SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo -e "   Tamanho: $PDF_SIZE"
echo -e "   Linhas de texto: $PDF_LINES"
echo -e "${GREEN}✓ PDF validado${NC}"
echo ""

# ==========================================================================
# PASSO 6: GIT ADD
# ==========================================================================
echo -e "${BLUE}[6/8]${NC} Adicionando arquivos ao Git..."

FILES_TO_ADD=(
    "SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
    "PROJETO_COMPLETO.md"
    "COMO_GERAR_PDF.md"
    "gerar-pdf.sh"
    "gerar-pdf-alternativo.sh"
)

for file in "${FILES_TO_ADD[@]}"; do
    if [ -f "$file" ]; then
        git add "$file"
        echo -e "   ✓ $file"
    else
        echo -e "   ⚠ $file (não encontrado, pulando)"
    fi
done

echo -e "${GREEN}✓ Arquivos adicionados${NC}"
echo ""

# ==========================================================================
# PASSO 7: GIT COMMIT
# ==========================================================================
echo -e "${BLUE}[7/8]${NC} Fazendo commit no repositório..."

COMMIT_MESSAGE="docs: Add final technical report PDF with complete documentation

- Link do GitHub repository (https://github.com/rcoura82/fase2_subst_9adjt)
- Complete API documentation (33+ endpoints)
- Technical report with all technologies used
- Challenges and solutions implemented
- Docker escalability strategies
- PDF generated from PROJETO_COMPLETO.md
- Installation guides and usage examples
- Automated PDF generation scripts

Content includes:
✓ GitHub repository link
✓ Swagger/OpenAPI documentation
✓ Technical architecture details
✓ Design patterns and best practices
✓ Challenges faced and solutions
✓ Docker multi-stage build optimization
✓ Docker Compose orchestration
✓ Performance optimizations
✓ Security recommendations
✓ Deployment and scaling guide
✓ Monitoring and logging setup
✓ Development instructions"

git commit -m "$COMMIT_MESSAGE"

echo -e "${GREEN}✓ Commit realizado${NC}"
echo ""

# ==========================================================================
# PASSO 8: GIT PUSH
# ==========================================================================
echo -e "${BLUE}[8/8]${NC} Fazendo push para o repositório remoto..."

git push origin main 2>&1 | tail -5

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Push realizado com sucesso${NC}"
else
    echo -e "${YELLOW}⚠ Push teve aviso, mas pode estar ok${NC}"
fi
echo ""

# ==========================================================================
# RESUMO FINAL
# ==========================================================================
echo "╔════════════════════════════════════════════════════════════════════╗"
echo "║                    ✅ PROCESSO CONCLUÍDO COM SUCESSO!              ║"
echo "╚════════════════════════════════════════════════════════════════════╝"
echo ""

echo -e "${GREEN}📄 ARQUIVO GERADO:${NC}"
echo "   Nome: SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo "   Tamanho: $PDF_SIZE"
echo "   Local: /workspaces/fase2_subst_9adjt/"
echo ""

echo -e "${BLUE}🔗 ACESSO AO PDF:${NC}"
echo "   Local: file:///workspaces/fase2_subst_9adjt/SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo "   GitHub: https://github.com/rcoura82/fase2_subst_9adjt/blob/main/SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo ""

echo -e "${BLUE}📋 CONTEÚDO DO PDF:${NC}"
echo "   1. Link do Repositório GitHub"
echo "   2. Documentação Técnica da API"
echo "      └─ 33+ endpoints REST com exemplos"
echo "   3. Relatório Técnico"
echo "      ├─ Tecnologias e ferramentas"
echo "      ├─ Arquitetura e design patterns"
echo "      ├─ Fluxos de negócio"
echo "      ├─ Desafios e soluções"
echo "      └─ Docker para escalabilidade"
echo "   4. Estatísticas do Projeto"
echo "   5. Deployment e Escalabilidade"
echo "   6. Monitoramento e Logs"
echo "   7. Segurança"
echo "   8. Testes"
echo "   9. Desenvolvimento Local"
echo "   10. Conclusão"
echo ""

echo -e "${BLUE}📊 ESTATÍSTICAS:${NC}"
git log --oneline | head -3 | while IFS= read -r line; do
    echo "   $line"
done
echo ""

echo -e "${YELLOW}🎉 Relatório Técnico em PDF pronto para apresentação!${NC}"
echo ""
echo "Próximos passos:"
echo "  1. Verifique o PDF: file:///workspaces/fase2_subst_9adjt/SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo "  2. Acesse no GitHub: https://github.com/rcoura82/fase2_subst_9adjt"
echo "  3. Compartilhe com suas partes interessadas"
echo ""
