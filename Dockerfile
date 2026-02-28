# Use official OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/myapp.jar app.jar
# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java","-jar","app.jar"]