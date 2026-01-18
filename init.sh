#!/bin/bash

# Script de inicialização rápida da Biblioteca Online

set -e

echo "=================================================="
echo "  Sistema de Biblioteca Online"
echo "  Inicialização Rápida"
echo "=================================================="
echo ""

# Verifica se Docker está instalado
if ! command -v docker &> /dev/null; then
    echo "❌ Docker não está instalado"
    echo "   Baixe em: https://www.docker.com/products/docker-desktop"
    exit 1
fi

# Verifica se Docker Compose está instalado
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose não está instalado"
    exit 1
fi

echo "✓ Docker e Docker Compose encontrados"
echo ""

# Oferece opções
echo "Escolha uma opção:"
echo ""
echo "1) Iniciar (build + compose up)"
echo "2) Parar containers"
echo "3) Ver logs da aplicação"
echo "4) Abrir Swagger UI no navegador"
echo "5) Resetar tudo (limpar dados)"
echo ""
read -p "Opção (1-5): " option

case $option in
    1)
        echo ""
        echo "🔨 Compilando imagem Docker..."
        docker-compose build --no-cache
        
        echo ""
        echo "🚀 Iniciando containers..."
        docker-compose up -d
        
        echo ""
        echo "⏳ Aguardando aplicação iniciar (30s)..."
        sleep 30
        
        echo ""
        echo "=================================================="
        echo "✅ Aplicação iniciada com sucesso!"
        echo "=================================================="
        echo ""
        echo "URLs de acesso:"
        echo "  API Swagger:  http://localhost:8080/api/swagger-ui.html"
        echo "  API Docs:     http://localhost:8080/api/v3/api-docs"
        echo "  PhpPgAdmin:   http://localhost:8081"
        echo ""
        echo "Banco de dados:"
        echo "  Host: localhost:5432"
        echo "  Usuário: postgres"
        echo "  Senha: postgres123"
        echo "  Banco: biblioteca_db"
        echo ""
        echo "Para ver logs: docker-compose logs -f app"
        echo "Para parar: docker-compose down"
        echo ""
        ;;
    2)
        echo ""
        echo "🛑 Parando containers..."
        docker-compose down
        echo "✅ Containers parados"
        echo ""
        ;;
    3)
        echo ""
        echo "📋 Logs da aplicação:"
        echo ""
        docker-compose logs -f app
        ;;
    4)
        echo ""
        echo "🌐 Abrindo Swagger UI..."
        
        # Verifica qual navegador está disponível
        if command -v xdg-open &> /dev/null; then
            xdg-open "http://localhost:8080/api/swagger-ui.html"
        elif command -v open &> /dev/null; then
            open "http://localhost:8080/api/swagger-ui.html"
        else
            echo "Abra manualmente: http://localhost:8080/api/swagger-ui.html"
        fi
        echo ""
        ;;
    5)
        echo ""
        echo "⚠️  AVISO: Esta ação irá remover TODOS os dados!"
        read -p "Tem certeza? (s/n): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Ss]$ ]]; then
            echo ""
            echo "🗑️  Removendo containers e volumes..."
            docker-compose down -v
            
            echo ""
            echo "🔨 Compilando imagem..."
            docker-compose build --no-cache
            
            echo ""
            echo "🚀 Iniciando..."
            docker-compose up -d
            
            echo ""
            echo "⏳ Aguardando (30s)..."
            sleep 30
            
            echo ""
            echo "✅ Sistema resetado e pronto!"
            echo ""
        else
            echo "❌ Operação cancelada"
        fi
        echo ""
        ;;
    *)
        echo "❌ Opção inválida"
        exit 1
        ;;
esac
