# 📚 Sistema de Biblioteca Online

Um **Sistema de Gerenciamento de Biblioteca** completo, desenvolvido em **Java 21** com **Spring Boot 3.2**, pronto para produção com Docker, APIs modernas e relatórios avançados.

## ✨ Destaques

- ✅ **33+ Endpoints REST** com documentação Swagger
- ✅ **5 Relatórios Avançados** usando Java Streams
- ✅ **3 Entidades JPA** com relacionamentos
- ✅ **Docker Pronto** com multi-estágio otimizado
- ✅ **PostgreSQL + H2** para diferentes ambientes
- ✅ **6 Documentações Completas** em Markdown
- ✅ **Validações Robustas** em todas as camadas

## 🚀 Início Rápido

### Com Docker (Recomendado)
```bash
docker-compose up -d --build
# Aguarde 30 segundos
# Acesse: http://localhost:8080/api/swagger-ui.html
```

### Com Script
```bash
chmod +x init.sh
./init.sh
# Selecione opção 1
```

### Desenvolvimento Local
```bash
mvn clean install
mvn spring-boot:run
# Acesse: http://localhost:8080/api/swagger-ui.html
```

## 📚 Funcionalidades

### 📕 Gestão de Livros
- CRUD completo (criar, buscar, atualizar, deletar)
- Filtros por título, autor, ISBN, categoria
- Listagem de livros disponíveis
- Ranking dos 20 livros mais emprestados

### 👤 Gestão de Usuários
- CRUD completo com validações
- Filtros por nome, email, tipo
- Ativar/desativar usuários
- Limite de empréstimos configurável (padrão: 5)

### 📕 Empréstimos e Devoluções
- Criar empréstimos com validações
- Período padrão: **14 dias**
- Timezone: **America/Sao_Paulo (GMT-3)**
- Renovação de empréstimos
- Detecção automática de atrasos
- Cálculo de dias em atraso

### 📊 Relatórios com Java Streams
1. **Top 20 Livros Mais Emprestados**
2. **Livros Emprestados** com devolução prevista
3. **Empréstimos por Usuário** com estatísticas
4. **Livros por Categoria** com disponibilidade
5. **Atividade em Período** com filtros de data

## 🏗️ Arquitetura

```
src/main/java/com/biblioteca/
├── entity/           ← Modelos JPA (Livro, Usuario, Emprestimo)
├── repository/       ← Acesso a dados (custom @Query)
├── service/          ← Lógica de negócio
├── controller/       ← APIs REST
├── dto/              ← Validações (Jakarta)
└── exception/        ← Tratamento centralizado
```

### Tecnologias

| Camada | Tecnologia |
|--------|-----------|
| **Linguagem** | Java 21 LTS |
| **Framework** | Spring Boot 3.2.0 |
| **ORM** | Hibernate + Spring Data JPA |
| **Banco Produção** | PostgreSQL 15 |
| **Banco Dev** | H2 em memória |
| **Validação** | Jakarta Bean Validation |
| **Documentação** | Swagger 3.0 / OpenAPI |
| **Container** | Docker + Docker Compose |
| **Build** | Maven 3.9+ |

## 📖 Documentação

| Documento | Descrição |
|-----------|-----------|
| **README.md** | Visão geral (este arquivo) |
| **GUIA_RAPIDO.md** | Quick start com exemplos |
| **INSTALACAO.md** | Passo-a-passo instalação |
| **RELATORIO_TECNICO.md** | Detalhes técnicos (14 seções) |
| **EXEMPLOS_REQUISICOES.md** | 30+ exemplos HTTP |
| **INDEX.md** | Índice completo |

## 🔗 Endpoints Principais

### Livros
```
POST   /api/livros                 # Criar
GET    /api/livros                 # Listar (com filtros)
GET    /api/livros/{id}            # Buscar por ID
GET    /api/livros/isbn/{isbn}     # Buscar por ISBN
PUT    /api/livros/{id}            # Atualizar
DELETE /api/livros/{id}            # Deletar
GET    /api/livros/disponibles     # Apenas disponíveis
GET    /api/livros/top-20          # Top 20 emprestados
```

### Usuários
```
POST   /api/usuarios               # Criar
GET    /api/usuarios               # Listar
GET    /api/usuarios/{id}          # Buscar por ID
GET    /api/usuarios/email/{email} # Buscar por email
PUT    /api/usuarios/{id}          # Atualizar
PATCH  /api/usuarios/{id}/ativar   # Ativar
PATCH  /api/usuarios/{id}/desativar # Desativar
DELETE /api/usuarios/{id}          # Deletar
```

### Empréstimos
```
POST   /api/emprestimos            # Criar empréstimo
GET    /api/emprestimos            # Listar
GET    /api/emprestimos/{id}       # Buscar por ID
PATCH  /api/emprestimos/{id}/devolver    # Devolver livro
PATCH  /api/emprestimos/{id}/renovar     # Renovar (+14 dias)
GET    /api/emprestimos/atrasados        # Listar atrasados
```

### Relatórios
```
GET /api/relatorios/top-20-livros-emprestados
GET /api/relatorios/livros-emprestados
GET /api/relatorios/emprestimos-por-usuario
GET /api/relatorios/livros-por-categoria
GET /api/relatorios/atividade-periodo?dataInicio=2026-01-01&dataFim=2026-12-31
```

## 🌐 URLs de Acesso

| Serviço | URL |
|---------|-----|
| **Swagger UI** | http://localhost:8080/api/swagger-ui.html |
| **OpenAPI JSON** | http://localhost:8080/api/v3/api-docs |
| **API Base** | http://localhost:8080/api |
| **PhpPgAdmin** | http://localhost:8081 |
| **PostgreSQL** | localhost:5432 |

## 🛠️ Configuração

### Variáveis de Ambiente (.env)
```env
DB_USERNAME=postgres
DB_PASSWORD=postgres123
DB_NAME=biblioteca_db
DB_PORT=5432
APP_PORT=8080
ADMIN_PORT=8081
```

### application.properties
```properties
# Banco de dados
spring.datasource.url=jdbc:postgresql://localhost:5432/biblioteca_db
spring.datasource.username=postgres
spring.datasource.password=postgres123
spring.jpa.hibernate.ddl-auto=update

# Timezone
spring.jackson.time-zone=America/Sao_Paulo

# Logging
logging.level.com.biblioteca=DEBUG
```

## 📊 Estatísticas

| Métrica | Valor |
|---------|-------|
| **Linhas de Código** | ~3.500 |
| **Classes Java** | 20+ |
| **Endpoints REST** | 33+ |
| **Relatórios** | 5 |
| **Documentação** | 6 arquivos |
| **Docker Image** | ~200MB |
| **Dependencies** | 15+ |

## ✅ Validações Implementadas

- ✅ Email único por usuário
- ✅ ISBN único por livro
- ✅ Cópias válidas e consistentes
- ✅ Livro disponível para empréstimo
- ✅ Usuário ativo (bloqueia empréstimo)
- ✅ Limite de empréstimos por usuário
- ✅ Atrasos bloqueiam novos empréstimos
- ✅ Datas automáticas (14 dias)
- ✅ Validação em DTOs (Jakarta)
- ✅ Validação em Service (lógica)

## 🐳 Docker

### Estrutura
```
docker-compose.yml
├── PostgreSQL 15      (porta 5432)
├── Spring Boot App    (porta 8080)
└── PhpPgAdmin         (porta 8081)
```

### Comandos Úteis
```bash
# Iniciar
docker-compose up -d --build

# Parar
docker-compose down

# Ver logs
docker-compose logs -f app

# Reiniciar
docker-compose restart

# Limpar dados
docker-compose down -v
```

## 🧪 Testando a API

### Com cURL
```bash
# Listar livros
curl http://localhost:8080/api/livros

# Criar livro
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Clean Code",
    "autor": "Robert Martin",
    "isbn": "978-0-13-235088-4",
    "categoria": "Programação",
    "copiasDisponiveis": 3,
    "copiasTotais": 5
  }'
```

### Com Swagger UI
1. Acesse http://localhost:8080/api/swagger-ui.html
2. Clique em um endpoint
3. Clique em "Try it out"
4. Preencha os dados
5. Clique em "Execute"

## 📝 Exemplos Completos

Veja [EXEMPLOS_REQUISICOES.md](EXEMPLOS_REQUISICOES.md) para 30+ exemplos HTTP detalhados.

## 🔐 Segurança (Recomendações Futuras)

- [ ] Implementar JWT Authentication
- [ ] Adicionar Role-Based Access Control (RBAC)
- [ ] Validar HTTPS em produção
- [ ] Implementar Rate Limiting
- [ ] Adicionar CORS configurável
- [ ] Encrypt senhas no banco

## 🤝 Contribuindo

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/amazing-feature`)
3. Commit suas mudanças (`git commit -m 'Add amazing feature'`)
4. Push para a branch (`git push origin feature/amazing-feature`)
5. Abra um Pull Request

## 📜 Licença

Este projeto está sob a licença MIT.

## 👤 Autor

**Ricardo Coura**
- GitHub: [@rcoura82](https://github.com/rcoura82)
- Projeto: [fase2_subst_9adjt](https://github.com/rcoura82/fase2_subst_9adjt)

## 📞 Suporte

- 📖 Documentação: Veja os 6 arquivos Markdown
- 🔍 API Interativa: http://localhost:8080/api/swagger-ui.html
- 💬 Issues: Abra uma issue no GitHub
- 📧 Email: [Contato]

---

**Status**: ✅ Pronto para Produção  
**Versão**: 1.0.0  
**Data**: Janeiro 2026  
**Java**: 21 LTS  
**Spring Boot**: 3.2.0  

🚀 **Pronto para começar? Execute `./init.sh` ou `docker-compose up -d`!**
