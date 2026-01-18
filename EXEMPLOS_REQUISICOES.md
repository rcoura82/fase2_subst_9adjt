# Exemplos de Requisições HTTP - Biblioteca Online

Este arquivo contém exemplos de requisições HTTP para testar a API.

**Base URL**: `http://localhost:8080/api`

---

## 📚 LIVROS

### 1. Criar Livro
```http
POST /api/livros
Content-Type: application/json

{
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "isbn": "978-0-13-235088-4",
  "descricao": "A Handbook of Agile Software Craftsmanship",
  "categoria": "Programação",
  "copiasDisponiveis": 3,
  "copiasTotais": 5
}
```

**Resposta 201 Created**:
```json
{
  "id": 1,
  "titulo": "Clean Code",
  "autor": "Robert C. Martin",
  "isbn": "978-0-13-235088-4",
  "descricao": "A Handbook of Agile Software Craftsmanship",
  "categoria": "Programação",
  "copiasDisponiveis": 3,
  "copiasTotais": 5,
  "dataCriacao": "2024-01-18T10:30:00",
  "dataAtualizacao": "2024-01-18T10:30:00"
}
```

### 2. Buscar Livro por ID
```http
GET /api/livros/1
```

### 3. Buscar Livro por ISBN
```http
GET /api/livros/isbn/978-0-13-235088-4
```

### 4. Listar Todos os Livros
```http
GET /api/livros?page=0&size=10
```

### 5. Listar com Filtros
```http
GET /api/livros?titulo=Clean&autor=Martin&categoria=Programação&page=0&size=10
```

### 6. Listar Livros Disponíveis
```http
GET /api/livros/disponíveis?page=0&size=10
```

### 7. Top 20 Livros Mais Emprestados
```http
GET /api/livros/mais-emprestados
```

### 8. Atualizar Livro
```http
PUT /api/livros/1
Content-Type: application/json

{
  "titulo": "Clean Code - Updated",
  "autor": "Robert C. Martin",
  "isbn": "978-0-13-235088-4",
  "descricao": "Updated description",
  "categoria": "Programação",
  "copiasDisponiveis": 4,
  "copiasTotais": 5
}
```

### 9. Deletar Livro
```http
DELETE /api/livros/1
```

---

## 👤 USUÁRIOS

### 1. Criar Usuário
```http
POST /api/usuarios
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "telefone": "(11) 98765-4321",
  "endereco": "Rua A, 123, São Paulo, SP",
  "tipoUsuario": "ALUNO",
  "ativo": true,
  "limiteEmprestimos": 5
}
```

**Resposta 201 Created**:
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@example.com",
  "telefone": "(11) 98765-4321",
  "endereco": "Rua A, 123, São Paulo, SP",
  "tipoUsuario": "ALUNO",
  "ativo": true,
  "limiteEmprestimos": 5,
  "dataCriacao": "2024-01-18T10:25:00",
  "dataAtualizacao": "2024-01-18T10:25:00"
}
```

### 2. Buscar Usuário por ID
```http
GET /api/usuarios/1
```

### 3. Buscar Usuário por Email
```http
GET /api/usuarios/email/joao.silva@example.com
```

### 4. Listar Todos os Usuários
```http
GET /api/usuarios?page=0&size=10
```

### 5. Buscar por Nome
```http
GET /api/usuarios?nome=João&page=0&size=10
```

### 6. Listar Usuários Ativos
```http
GET /api/usuarios/ativos?page=0&size=10
```

### 7. Buscar por Tipo de Usuário
```http
GET /api/usuarios?tipo=ALUNO&page=0&size=10
```

**Tipos válidos**: ALUNO, PROFESSOR, VISITANTE, FUNCIONARIO

### 8. Atualizar Usuário
```http
PUT /api/usuarios/1
Content-Type: application/json

{
  "nome": "João Silva Atualizado",
  "email": "joao.silva@example.com",
  "telefone": "(11) 98765-4321",
  "endereco": "Rua B, 456, São Paulo, SP",
  "tipoUsuario": "ALUNO",
  "ativo": true,
  "limiteEmprestimos": 6
}
```

### 9. Ativar Usuário
```http
PATCH /api/usuarios/1/ativar
```

### 10. Desativar Usuário
```http
PATCH /api/usuarios/1/desativar
```

### 11. Deletar Usuário
```http
DELETE /api/usuarios/1
```

---

## 📕 EMPRÉSTIMOS

### 1. Criar Empréstimo
```http
POST /api/emprestimos
Content-Type: application/json

{
  "livroId": 1,
  "usuarioId": 1
}
```

**Resposta 201 Created**:
```json
{
  "id": 1,
  "livroId": 1,
  "usuarioId": 1,
  "dataEmprestimo": "2024-01-18",
  "dataDeVolucaoPrevista": "2024-02-01",
  "dataDeVolucaoReal": null,
  "status": "ATIVO",
  "observacoes": null,
  "diasAtrasados": 0
}
```

### 2. Buscar Empréstimo por ID
```http
GET /api/emprestimos/1
```

### 3. Listar Empréstimos Ativos
```http
GET /api/emprestimos/ativos?page=0&size=10
```

### 4. Listar Empréstimos Atrasados
```http
GET /api/emprestimos/atrasados?page=0&size=10
```

### 5. Empréstimos de um Usuário
```http
GET /api/emprestimos/usuario/1?status=ATIVO&page=0&size=10
```

### 6. Empréstimos de um Livro
```http
GET /api/emprestimos/livro/1?page=0&size=10
```

### 7. Empréstimos por Período
```http
GET /api/emprestimos/periodo?dataInicio=2024-01-01&dataFim=2024-01-31&page=0&size=10
```

### 8. Histórico de Empréstimos do Usuário
```http
GET /api/emprestimos/usuario/1/historico?dataInicio=2024-01-01&dataFim=2024-01-31
```

### 9. Empréstimos Ativos do Usuário
```http
GET /api/emprestimos/usuario/1/ativos
```

### 10. Devolver Livro
```http
PATCH /api/emprestimos/1/devolver
```

**Resposta 200 OK**:
```json
{
  "id": 1,
  "livroId": 1,
  "usuarioId": 1,
  "dataEmprestimo": "2024-01-18",
  "dataDeVolucaoPrevista": "2024-02-01",
  "dataDeVolucaoReal": "2024-01-20",
  "status": "DEVOLVIDO",
  "observacoes": null,
  "diasAtrasados": 0
}
```

### 11. Renovar Empréstimo
```http
PATCH /api/emprestimos/1/renovar
```

**Resposta 200 OK**:
```json
{
  "id": 1,
  "livroId": 1,
  "usuarioId": 1,
  "dataEmprestimo": "2024-01-18",
  "dataDeVolucaoPrevista": "2024-02-15",
  "dataDeVolucaoReal": null,
  "status": "ATIVO",
  "observacoes": null,
  "diasAtrasados": 0
}
```

### 12. Deletar Empréstimo
```http
DELETE /api/emprestimos/1
```

---

## 📊 RELATÓRIOS

### 1. Top 20 Livros Mais Emprestados
```http
GET /api/relatorios/top-20-livros-emprestados
```

**Resposta**:
```json
[
  {
    "id": 1,
    "titulo": "Clean Code",
    "autor": "Robert C. Martin",
    "isbn": "978-0-13-235088-4",
    "categoria": "Programação",
    "copiasDisponiveis": 2,
    "copiasTotais": 5,
    "quantidadeEmprestimos": 3
  },
  {
    "id": 2,
    "titulo": "Design Patterns",
    "autor": "Gang of Four",
    "isbn": "978-0-201-63361-0",
    "categoria": "Programação",
    "copiasDisponiveis": 1,
    "copiasTotais": 3,
    "quantidadeEmprestimos": 2
  }
]
```

### 2. Livros Emprestados com Previsão de Devolução
```http
GET /api/relatorios/livros-emprestados
```

**Resposta**:
```json
[
  {
    "emprestimoId": 1,
    "livroId": 1,
    "livroTitulo": "Clean Code",
    "livroAutor": "Robert C. Martin",
    "livroIsbn": "978-0-13-235088-4",
    "usuarioId": 1,
    "usuarioNome": "João Silva",
    "usuarioEmail": "joao.silva@example.com",
    "dataEmprestimo": "2024-01-18",
    "dataDeVolucaoPrevista": "2024-02-01",
    "diasRestantes": 14,
    "estaAtrasado": false,
    "diasAtrasados": 0
  }
]
```

### 3. Empréstimos por Usuário
```http
GET /api/relatorios/emprestimos-por-usuario
```

**Resposta**:
```json
[
  {
    "usuarioId": 1,
    "usuarioNome": "João Silva",
    "usuarioEmail": "joao.silva@example.com",
    "usuarioTipo": "ALUNO",
    "totalEmprestimos": 5,
    "emprestimosDevolvidosCount": 3,
    "emprestimosAtivosCount": 2,
    "emprestimosAtrasadosCount": 0
  }
]
```

### 4. Livros por Categoria
```http
GET /api/relatorios/livros-por-categoria
```

**Resposta**:
```json
[
  {
    "categoria": "Programação",
    "totalLivros": 12,
    "copiasDisponiveis": 8,
    "copiasTotais": 20,
    "taxaDisponibilidade": 40.0,
    "livros": [
      {
        "id": 1,
        "titulo": "Clean Code",
        "autor": "Robert C. Martin",
        "isbn": "978-0-13-235088-4"
      }
    ]
  }
]
```

### 5. Atividade em Período
```http
GET /api/relatorios/atividade-periodo?dataInicio=2024-01-01&dataFim=2024-01-31
```

**Resposta**:
```json
{
  "dataInicio": "2024-01-01",
  "dataFim": "2024-01-31",
  "totalEmprestimos": 10,
  "totalDevolucoes": 8,
  "emprestimoAtrasados": 1,
  "taxaAtraso": 10.0
}
```

---

## 🔍 EXEMPLOS COM cURL

### Criar Livro
```bash
curl -X POST http://localhost:8080/api/livros \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Effective Java",
    "autor": "Joshua Bloch",
    "isbn": "978-0-13-468599-1",
    "categoria": "Programação",
    "copiasDisponiveis": 2,
    "copiasTotais": 3
  }'
```

### Criar Usuário
```bash
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "email": "maria.santos@example.com",
    "tipoUsuario": "PROFESSOR",
    "limiteEmprestimos": 10
  }'
```

### Criar Empréstimo
```bash
curl -X POST http://localhost:8080/api/emprestimos \
  -H "Content-Type: application/json" \
  -d '{
    "livroId": 1,
    "usuarioId": 1
  }'
```

### Devolver Livro
```bash
curl -X PATCH http://localhost:8080/api/emprestimos/1/devolver
```

### Listar com Filtros
```bash
curl "http://localhost:8080/api/livros?titulo=Clean&page=0&size=10"
```

### Com Autenticação (Futura implementação)
```bash
curl -H "Authorization: Bearer TOKEN" \
  http://localhost:8080/api/livros
```

---

## ⚠️ CÓDIGOS DE ERRO ESPERADOS

| Código | Erro | Solução |
|--------|------|---------|
| 200 | OK | Sucesso |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Sucesso sem retorno |
| 400 | Bad Request | Dados inválidos ou validação falhou |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Email ou ISBN duplicado |
| 422 | Unprocessable Entity | Validação de negócio falhou |
| 500 | Internal Server Error | Erro na aplicação |

---

## 📝 DICAS PARA TESTES

1. **Use Postman ou Insomnia**: Esses clientes HTTP facilitam testes
2. **Importe do Swagger**: Copie a URL `http://localhost:8080/api/v3/api-docs` no Postman
3. **Teste com paginação**: Sempre use `page=0&size=10` para resultados grandes
4. **Verifique timestamps**: Datas estão em ISO-8601 (2024-01-18T10:30:00)
5. **Testes de carga**: Use `apache-bench` ou `wrk` para testes de performance

---

## 🚀 SCRIPTS DE TESTE COMPLETO

### setup.sh - Preencher banco com dados
```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

# Criar 3 livros
for i in {1..3}; do
  curl -X POST $BASE_URL/livros \
    -H "Content-Type: application/json" \
    -d "{
      \"titulo\": \"Livro $i\",
      \"autor\": \"Autor $i\",
      \"isbn\": \"978-0-13-23508$i\",
      \"categoria\": \"Programação\",
      \"copiasDisponiveis\": 2,
      \"copiasTotais\": 3
    }"
  echo ""
done

# Criar 3 usuários
for i in {1..3}; do
  curl -X POST $BASE_URL/usuarios \
    -H "Content-Type: application/json" \
    -d "{
      \"nome\": \"Usuário $i\",
      \"email\": \"usuario$i@example.com\",
      \"tipoUsuario\": \"ALUNO\",
      \"limiteEmprestimos\": 5
    }"
  echo ""
done

echo "✅ Dados de teste criados!"
```

---

**Última atualização**: Janeiro 2024
