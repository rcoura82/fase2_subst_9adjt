#!/bin/bash

# Script para gerar PDF do Relatório Técnico
# Convertendo PROJETO_COMPLETO.md para PDF

set -e

echo "🚀 Iniciando geração do PDF do Relatório Técnico..."
echo ""

# Cores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Diretório do projeto
PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$PROJECT_DIR"

echo -e "${BLUE}📁 Diretório: $PROJECT_DIR${NC}"
echo ""

# Passo 1: Verificar se pandoc está instalado
echo -e "${YELLOW}[1/4]${NC} Verificando pandoc..."
if ! command -v pandoc &> /dev/null; then
    echo -e "${YELLOW}⚠️  Pandoc não encontrado. Instalando...${NC}"
    sudo apt-get update -qq
    sudo apt-get install -y -qq pandoc pandoc-latex-environment texlive-latex-base texlive-fonts-recommended
    echo -e "${GREEN}✓ Pandoc instalado${NC}"
else
    echo -e "${GREEN}✓ Pandoc já está instalado${NC}"
    pandoc --version | head -n 1
fi
echo ""

# Passo 2: Verificar se arquivo markdown existe
echo -e "${YELLOW}[2/4]${NC} Verificando arquivo markdown..."
if [ ! -f "PROJETO_COMPLETO.md" ]; then
    echo -e "${RED}✗ Arquivo PROJETO_COMPLETO.md não encontrado!${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Arquivo PROJETO_COMPLETO.md encontrado${NC}"
echo ""

# Passo 3: Converter para PDF
echo -e "${YELLOW}[3/4]${NC} Convertendo markdown para PDF..."
echo -e "   Arquivo: PROJETO_COMPLETO.md"
echo -e "   Saída: SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"

pandoc PROJETO_COMPLETO.md \
    -o SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf \
    --pdf-engine=pdflatex \
    --toc \
    --toc-depth=3 \
    -V colorlinks=true \
    -V linkcolor=blue \
    -V geometry:margin=1in \
    -V fontsize=11pt \
    2>&1 | head -20

echo -e "${GREEN}✓ Conversão concluída${NC}"
echo ""

# Passo 4: Verificar se PDF foi criado
echo -e "${YELLOW}[4/4]${NC} Verificando arquivo PDF..."
if [ -f "SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf" ]; then
    PDF_SIZE=$(ls -lh SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf | awk '{print $5}')
    echo -e "${GREEN}✓ PDF criado com sucesso!${NC}"
    echo -e "  📄 Arquivo: SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
    echo -e "  💾 Tamanho: $PDF_SIZE"
else
    echo -e "${RED}✗ Erro na criação do PDF${NC}"
    exit 1
fi
echo ""

# Passo 5: Fazer commit
echo -e "${BLUE}📤 Fazendo commit no Git...${NC}"
git add SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf

git commit -m "docs: Add final technical report PDF with complete documentation

- Complete GitHub repository link
- Technical API documentation (Swagger/OpenAPI endpoints)
- Technical report with technologies and challenges
- Solutions implemented with Docker focus
- Project statistics and deployment guide
- 10 comprehensive sections covering all aspects

PDF contains:
✓ GitHub repository link
✓ API documentation (33+ endpoints)
✓ Technical architecture details
✓ Challenges and solutions
✓ Docker scaling strategies
✓ Deployment instructions
✓ Security recommendations
✓ Performance optimizations
✓ Monitoring and logging setup
✓ Development guide" || true

git push origin main

echo ""
echo -e "${GREEN}✅ PDF gerado e commitado com sucesso!${NC}"
echo ""
echo -e "${BLUE}📥 Informações do Arquivo:${NC}"
echo -e "  Nome: SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo -e "  Local: /workspaces/fase2_subst_9adjt/"
echo -e "  GitHub: https://github.com/rcoura82/fase2_subst_9adjt/blob/main/SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"
echo ""
echo -e "${BLUE}📄 Conteúdo do PDF:${NC}"
echo "  1. Link do Repositório GitHub"
echo "  2. Documentação Técnica da API"
echo "     - 33+ endpoints REST"
echo "     - Estrutura de respostas"
echo "     - Validações"
echo "     - Códigos HTTP"
echo "  3. Relatório Técnico"
echo "     - Tecnologias e ferramentas"
echo "     - Arquitetura e design patterns"
echo "     - Desafios e soluções"
echo "     - Docker para escalabilidade"
echo "  4. Estatísticas do projeto"
echo "  5. Deployment e escalabilidade"
echo "  6. Monitoramento"
echo "  7. Segurança"
echo "  8. Testes"
echo "  9. Desenvolvimento local"
echo "  10. Conclusão"
echo ""
echo -e "${GREEN}🎉 Pronto para apresentação!${NC}"
