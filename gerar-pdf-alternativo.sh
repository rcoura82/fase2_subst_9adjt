#!/bin/bash

# Script alternativo para gerar PDF (sem pandoc, usando wkhtmltopdf)
# Se pandoc não funcionar, este é o plano B

set -e

cd /workspaces/fase2_subst_9adjt

echo "🚀 Iniciando geração do PDF (Plano B - wkhtmltopdf)..."
echo ""

# Instalar wkhtmltopdf se necessário
if ! command -v wkhtmltopdf &> /dev/null; then
    echo "⚠️  Instalando wkhtmltopdf..."
    apt-get update -qq
    apt-get install -y -qq wkhtmltopdf xvfb fontconfig xfonts-encodings xfonts-utils
fi

echo "✓ Gerando PDF..."

# Converter markdown para HTML primeiro
cat > /tmp/relatorio.html << 'EOF'
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sistema de Biblioteca Online - Relatório Técnico</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            margin: 20px;
        }
        h1 { color: #2c3e50; border-bottom: 3px solid #3498db; padding-bottom: 10px; }
        h2 { color: #34495e; margin-top: 20px; border-left: 4px solid #3498db; padding-left: 10px; }
        h3 { color: #7f8c8d; }
        code { background: #ecf0f1; padding: 2px 4px; border-radius: 3px; }
        pre { background: #2c3e50; color: #ecf0f1; padding: 10px; border-radius: 5px; overflow-x: auto; }
        table { border-collapse: collapse; width: 100%; margin: 10px 0; }
        th, td { border: 1px solid #bdc3c7; padding: 8px; text-align: left; }
        th { background: #3498db; color: white; }
        tr:nth-child(even) { background: #ecf0f1; }
        .highlight { background: #fff3cd; padding: 15px; border-left: 4px solid #ffc107; margin: 10px 0; }
        .success { color: #27ae60; }
        .warning { color: #f39c12; }
    </style>
</head>
<body>
EOF

# Adicionar conteúdo do markdown convertido para HTML
cat PROJETO_COMPLETO.md | sed 's/^# /\n<h1>/g; s/^## /<h2>/g; s/^### /<h3>/g; s/$/\n<\/h1><\/h2><\/h3>/g' >> /tmp/relatorio.html

cat >> /tmp/relatorio.html << 'EOF'
</body>
</html>
EOF

# Converter HTML para PDF com wkhtmltopdf
xvfb-run --auto-servernum --server-args="-screen 0 1024x768x24" \
    wkhtmltopdf --quiet \
    --dpi 300 \
    --lowquality \
    -T 15mm -B 15mm -L 15mm -R 15mm \
    /tmp/relatorio.html \
    SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf

echo "✓ PDF criado: SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf"

# Cleanup
rm -f /tmp/relatorio.html

# Git commit
git add SISTEMA_BIBLIOTECA_ONLINE_RELATORIO_FINAL.pdf
git commit -m "docs: Add final technical report PDF" || true
git push origin main

echo "✅ Concluído!"
