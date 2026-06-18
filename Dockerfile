# ============================================================
# Stage 1: Build the WAR with Maven
# ============================================================
FROM maven:3.9-eclipse-temurin-11 AS builder

WORKDIR /app

# Copy only pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build (skip tests during image build — run them in CI)
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Deploy to Tomcat
# ============================================================
FROM tomcat:9.0-jdk11-openjdk-slim

# Remove the default ROOT webapp
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy WAR from builder stage
COPY --from=builder /app/target/web-customer-tracker.war \
     /usr/local/tomcat/webapps/web-customer-tracker.war

# Tomcat memory tuning
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseContainerSupport"

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/web-customer-tracker/customer/list || exit 1

CMD ["catalina.sh", "run"]
