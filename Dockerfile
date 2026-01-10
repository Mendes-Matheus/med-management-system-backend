# Etapa 1: Build
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml ./

# Baixa dependências antes de copiar o código → aproveita cache
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
EXPOSE 8080

# Copia apenas o jar compilado
COPY --from=build /app/target/*.jar app.jar

# Usa variável de ambiente padrão do Render
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
