# Dockerfile para aplicação Biblioteca Online
# Construído em múltiplos estágios para otimizar tamanho

# Estágio 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# Copia arquivos de dependência
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia código-fonte
COPY src ./src

# Compila a aplicação
RUN mvn clean package -DskipTests

# Estágio 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Instala ferramentas úteis
RUN apk add --no-cache curl

# Copia JAR do estágio anterior
COPY --from=builder /app/target/biblioteca-online-*.jar app.jar

# Expõe porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8080/api/livros || exit 1

# Variáveis de ambiente padrão
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"
ENV SPRING_PROFILES_ACTIVE=production
ENV TZ=America/Sao_Paulo

# Executa aplicação
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar app.jar"]
