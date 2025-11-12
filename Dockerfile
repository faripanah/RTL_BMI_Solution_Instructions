FROM openjdk:21-jdk-slim
ENV DISPLAY=host.docker.internal:0.0

# Install required dependencies
RUN apt-get update && \
    apt-get install -y maven wget unzip \
    libgtk-3-0 libgbm1 libx11-6 && \
    apt-get clean

# Download and install JavaFX SDK
RUN wget https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip -O /tmp/openjfx.zip && \
    unzip /tmp/openjfx.zip -d /opt && \
    rm /tmp/openjfx.zip

# List the contents of /opt to confirm where the JavaFX SDK was extracted
RUN ls -l /opt

# Set working directory
WORKDIR /app

# Copy the project files into the containerm
COPY pom.xml /app/
COPY src /app/src

# Build the application (with dependencies bundled into the JAR)
RUN mvn clean package -DskipTests

# List contents of target folder to ensure the JAR is created
RUN ls -l target/

# List contents of the JavaFX SDK to ensure correct extraction
RUN ls -l /opt/javafx-sdk-21 || ls -l /opt

# Set the command to run the JavaFX application with the correct module path
CMD ["java", "--module-path", "/opt/javafx-sdk-21/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/bmidemo.jar"]
