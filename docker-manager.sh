#!/bin/bash

# Script para gerenciar a aplicação Biblioteca Online com Docker

set -e

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função de ajuda
usage() {
    echo -e "${BLUE}Biblioteca Online - Docker Manager${NC}"
    echo ""
    echo "Uso: $0 [comando]"
    echo ""
    echo "Comandos disponíveis:"
    echo "  build       - Compila a imagem Docker"
    echo "  up          - Inicia os containers"
    echo "  down        - Para os containers"
    echo "  restart     - Reinicia os containers"
    echo "  logs        - Mostra logs da aplicação"
    echo "  logs-db     - Mostra logs do banco de dados"
    echo "  ps          - Lista containers em execução"
    echo "  shell       - Abre shell na aplicação"
    echo "  shell-db    - Abre shell no banco de dados"
    echo "  clean       - Remove containers e volumes"
    echo "  reset       - Remove tudo e reconstrói do zero"
    echo "  status      - Mostra status dos containers"
    echo "  help        - Mostra esta mensagem"
    echo ""
}

# Função para build
build() {
    echo -e "${BLUE}Building Docker image...${NC}"
    docker-compose build --no-cache
    echo -e "${GREEN}✓ Build concluído com sucesso${NC}"
}

# Função para up
up() {
    echo -e "${BLUE}Iniciando containers...${NC}"
    docker-compose up -d
    echo -e "${GREEN}✓ Containers iniciados${NC}"
    sleep 3
    status
}

# Função para down
down() {
    echo -e "${BLUE}Parando containers...${NC}"
    docker-compose down
    echo -e "${GREEN}✓ Containers parados${NC}"
}

# Função para restart
restart() {
    echo -e "${BLUE}Reiniciando containers...${NC}"
    docker-compose restart
    echo -e "${GREEN}✓ Containers reiniciados${NC}"
    sleep 2
    status
}

# Função para logs
logs() {
    docker-compose logs -f app
}

# Função para logs do banco
logs_db() {
    docker-compose logs -f postgres
}

# Função para ps
ps() {
    echo -e "${BLUE}Containers em execução:${NC}"
    docker-compose ps
}

# Função para shell da app
shell() {
    docker-compose exec app sh
}

# Função para shell do banco
shell_db() {
    docker-compose exec postgres psql -U postgres -d biblioteca_db
}

# Função para clean
clean() {
    echo -e "${YELLOW}Removendo containers e volumes...${NC}"
    docker-compose down -v
    echo -e "${GREEN}✓ Limpeza concluída${NC}"
}

# Função para reset
reset() {
    echo -e "${RED}AVISO: Esta ação irá remover TODOS os dados!${NC}"
    read -p "Tem certeza? (s/n): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Ss]$ ]]; then
        clean
        build
        up
        echo -e "${GREEN}✓ Reset concluído. Sistema pronto!${NC}"
    else
        echo -e "${YELLOW}Operação cancelada${NC}"
    fi
}

# Função para status
status() {
    echo -e "${BLUE}Status dos containers:${NC}"
    docker-compose ps
    echo ""
    echo -e "${BLUE}URLs de acesso:${NC}"
    echo -e "  API (Swagger): ${GREEN}http://localhost:8080/api/swagger-ui.html${NC}"
    echo -e "  API Docs: ${GREEN}http://localhost:8080/api/v3/api-docs${NC}"
    echo -e "  PhpPgAdmin: ${GREEN}http://localhost:8081${NC}"
    echo ""
}

# Executar comando
case "${1:-help}" in
    build)
        build
        ;;
    up)
        build
        up
        ;;
    down)
        down
        ;;
    restart)
        restart
        ;;
    logs)
        logs
        ;;
    logs-db)
        logs_db
        ;;
    ps)
        ps
        ;;
    shell)
        shell
        ;;
    shell-db)
        shell_db
        ;;
    clean)
        clean
        ;;
    reset)
        reset
        ;;
    status)
        status
        ;;
    help|*)
        usage
        ;;
esac
