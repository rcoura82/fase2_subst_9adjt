# GUIA DE INSTALAÇÃO E COMPILAÇÃO

## Pré-requisitos

### Opção 1: Com Docker (Recomendado)
- Docker 20.10 ou superior
- Docker Compose 2.0 ou superior
- ~500MB de espaço em disco

### Opção 2: Sem Docker (Desenvolvimento)
- Java 21 LTS
- Maven 3.9+
- PostgreSQL 15+ (opcional, pode usar H2)
- ~2GB de espaço em disco

---

## INSTALAÇÃO COM DOCKER (Recomendado)

### 1. Clone o Repositório

```bash
git clone https://github.com/rcoura82/fase2_subst_9adjt.git
cd fase2_subst_9adjt
```

### 2. Inicie os Containers

#### Opção A: Script Rápido (Recomendado)

```bash
chmod +x init.sh
./init.sh

# Selecione opção "1" para iniciar
```

#### Opção B: Docker Compose Direto

```bash
# Build da imagem
docker-compose build --no-cache

# Inicie os containers
docker-compose up -d

# Verifique o status
docker-compose ps
```

#### Opção C: Docker Manager Script

```bash
chmod +x docker-manager.sh
./docker-manager.sh up
```

### 3. Aguarde a Inicialização

A aplicação pode levar 30-45 segundos para iniciar. Verifique o status:

```bash
# Ver logs em tempo real
docker-compose logs -f app

# Quando vir "Started BibliotecaOnlineApplication", está pronto!
```

### 4. Acesse a API

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs
- **PhpPgAdmin**: http://localhost:8081

### 5. Teste a API

```bash
# Listar livros
curl http://localhost:8080/api/livros

# Criar um livro
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "isbn": "978-0-13-235088-4",
    "categoria": "Tecnologia",
    "copiasDisponiveis": 3,
    "copiasTotais": 5
  }'
```

---

## INSTALAÇÃO SEM DOCKER (Desenvolvimento Local)

### 1. Pré-requisitos

#### Windows
```powershell
# Verificar Java
java -version

# Verificar Maven
mvn -v

# Instalar se necessário:
# - Java: https://adoptium.net/
# - Maven: https://maven.apache.org/download.cgi
```

#### Linux/Mac
```bash
# Ubuntu/Debian
sudo apt update
sudo apt install openjdk-21-jdk maven

# Mac (com Homebrew)
brew install openjdk@21 maven
```

### 2. Clone o Repositório

```bash
git clone https://github.com/rcoura82/fase2_subst_9adjt.git
cd fase2_subst_9adjt
```

### 3. Configure o Banco de Dados

#### Opção A: H2 (Em Memória - Padrão)

Nenhuma configuração necessária! O H2 é automático.

```properties
# src/main/resources/application.properties já configurado
spring.datasource.url=jdbc:h2:mem:bibliodb
```

#### Opção B: PostgreSQL Local

```bash
# Instalar PostgreSQL
# Windows: https://www.postgresql.org/download/windows/
# Linux: sudo apt install postgresql postgresql-contrib
# Mac: brew install postgresql

# Inicie o PostgreSQL
sudo systemctl start postgresql  # Linux
brew services start postgresql   # Mac

# Crie o banco de dados
psql -U postgres -c "CREATE DATABASE biblioteca_db;"

# Atualize application.properties
# Descomente as linhas de PostgreSQL e comente H2
```

### 4. Compile o Projeto

```bash
# Baixe dependências e compile
mvn clean install

# Ou apenas compile (sem testes)
mvn clean package -DskipTests
```

### 5. Execute a Aplicação

```bash
# Opção A: Com Maven
mvn spring-boot:run

# Opção B: Com Java direto
java -jar target/biblioteca-online-1.0.0.jar

# Opção C: Na IDE (Eclipse, IntelliJ, VS Code)
# Clique direito no projeto > Run As > Spring Boot App
```

### 6. Aguarde o Startup

```
........ (mais linhas de log)
2024-01-18 10:30:45.123  INFO 12345 --- [           main] 
c.b.BibliotecaOnlineApplication         : Started BibliotecaOnlineApplication 
in 4.532 seconds (JVM running for 4.892)
```

Quando ver "Started BibliotecaOnlineApplication", acesse:

- **Swagger**: http://localhost:8080/api/swagger-ui.html
- **API**: http://localhost:8080/api/livros

---

## COMANDOS ÚTEIS

### Docker Compose

```bash
# Ver status dos containers
docker-compose ps

# Ver logs
docker-compose logs -f app           # Logs da app
docker-compose logs -f postgres      # Logs do banco

# Parar containers
docker-compose down

# Remover tudo (dados também)
docker-compose down -v

# Reiniciar
docker-compose restart

# Entrar em um container
docker-compose exec app sh           # Shell da app
docker-compose exec postgres psql -U postgres  # SQL shell
```

### Maven

```bash
# Compile e instale dependências
mvn clean install

# Execute testes
mvn test

# Gere Javadoc
mvn javadoc:javadoc

# Limpe artifacts
mvn clean
```

### Verificação de Saúde

```bash
# Verificar se a aplicação está rodando
curl http://localhost:8080/api/livros

# Com verbose
curl -v http://localhost:8080/api/swagger-ui.html

# Verificar banco de dados
docker-compose exec postgres psql -U postgres -d biblioteca_db -c "SELECT * FROM usuarios;"
```

---

## TROUBLESHOOTING

### Problema: Porta 8080 já está em uso

**Solução 1**: Mudar a porta na `docker-compose.yml`
```yaml
ports:
  - "8888:8080"  # Mude para outra porta
```

**Solução 2**: Matar processo na porta 8080
```bash
# Linux/Mac
lsof -i :8080
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Problema: Porta 5432 já está em uso

**Solução**: Mudar porta do PostgreSQL
```yaml
services:
  postgres:
    ports:
      - "5433:5432"  # Mude para 5433
```

E atualize `docker-compose.yml`:
```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5433/biblioteca_db
```

### Problema: Docker não consegue conectar ao banco

**Solução**: Aguarde a inicialização do PostgreSQL
```bash
# O docker-compose.yml já tem health checks
# Aguarde ~15 segundos antes de testar

docker-compose logs postgres  # Veja os logs do banco
```

### Problema: Erro "connection refused" na aplicação

**Solução 1**: Verifique se PostgreSQL está rodando
```bash
docker-compose ps

# STATUS deve ser "Up" com health check "healthy"
```

**Solução 2**: Reinicie tudo
```bash
docker-compose down -v
docker-compose up -d --build
```

### Problema: Aplicação consome muita memória

**Solução**: Ajuste a JVM no `docker-compose.yml`
```yaml
JAVA_OPTS: "-Xms256m -Xmx512m"  # Reduz de 512-1024 para 256-512
```

### Problema: Swagger UI não funciona

**Solução**: Limpe cache do navegador
```bash
# Ou tente incognito
Ctrl+Shift+Delete  # Windows/Linux
Cmd+Shift+Delete   # Mac
```

---

## VERIFICAÇÃO POS-INSTALAÇÃO

### Checklist Final

- [ ] Docker rodando (`docker ps`)
- [ ] Containers saudáveis (`docker-compose ps`)
- [ ] Aplicação respondendo (`curl http://localhost:8080/api/livros`)
- [ ] Swagger acessível (`http://localhost:8080/api/swagger-ui.html`)
- [ ] Banco de dados conectado (sem erros nos logs)
- [ ] Consegue criar um livro (POST /api/livros)

### Script de Teste

```bash
#!/bin/bash

echo "🧪 Testando Sistema de Biblioteca..."
echo ""

# Teste 1: Aplicação respondendo
echo -n "1. Aplicação respondendo... "
if curl -s http://localhost:8080/api/livros > /dev/null; then
    echo "✅"
else
    echo "❌"
    exit 1
fi

# Teste 2: Banco acessível
echo -n "2. Banco de dados... "
if docker-compose exec -T postgres psql -U postgres -d biblioteca_db -c "SELECT 1;" > /dev/null 2>&1; then
    echo "✅"
else
    echo "❌"
    exit 1
fi

# Teste 3: Criar livro
echo -n "3. Criar livro... "
RESPONSE=$(curl -s -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Test Book",
    "autor": "Test Author",
    "isbn": "999-9999999999",
    "categoria": "Teste",
    "copiasDisponiveis": 1,
    "copiasTotais": 1
  }')

if echo "$RESPONSE" | grep -q "999-9999999999"; then
    echo "✅"
else
    echo "❌"
fi

echo ""
echo "✅ Todos os testes passaram!"
```

---

## PRÓXIMOS PASSOS

1. Acesse o Swagger: http://localhost:8080/api/swagger-ui.html
2. Crie alguns livros de teste
3. Crie usuários
4. Faça empréstimos
5. Consulte os relatórios

---

## SUPORTE

- GitHub Issues: https://github.com/rcoura82/fase2_subst_9adjt/issues
- Documentação Swagger: http://localhost:8080/api/swagger-ui.html
- Relatório Técnico: `RELATORIO_TECNICO.md`

---

**Última atualização**: Janeiro 2024
