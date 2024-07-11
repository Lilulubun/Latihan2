
# Urbanify

Urbanify is a JavaFX-based application that helps manage and display user data in a graphical interface.

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven
- JavaFX SDK (version 21)

## Building the Project

To build the project, follow these steps:

1. **Clone the repository**:
    ```sh
    git clone <repository-url>
    cd Urbanify
    ```

2. **Download the JavaFX SDK**:
    Download the JavaFX SDK from the [Gluon website](https://gluonhq.com/products/javafx/) and extract it to a known location. Set the `PATH_TO_FX` environment variable to the location of the extracted JavaFX SDK.
    If you have already, skip this step

4. **Build the project**:
    ```sh
    mvn clean package
    ```
    After building the project there will be target folder.

## Running the Application

### Running with Maven

You can run the application directly using Maven:

```sh
mvn javafx:run
```

### Running the JAR File

To run the JAR file, use the following command:

```sh
java --module-path $PATH_TO_FX/lib --add-modules javafx.controls,javafx.fxml -jar target/Urbanify-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Make sure to replace `$PATH_TO_FX` with the actual path to your JavaFX SDK.

## Project Structure

- `src/main/java`: Contains the Java source files.
- `src/main/resources`: Contains the resource files (e.g., FXML files, CSV files).
- `target`: Contains the built artifacts.

## Dependencies

- JavaFX Controls
- JavaFX FXML
- JUnit 5
- ControlsFX

## Maven Configuration

The Maven configuration for this project includes the necessary plugins and dependencies to build and package the application with JavaFX:

```xml
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>21</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>21</version>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.controlsfx</groupId>
        <artifactId>controlsfx</artifactId>
        <version>11.1.2</version>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-assembly-plugin</artifactId>
            <version>3.3.0</version>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>com.example.latihan2.MainApp</mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.11.0</version>
            <configuration>
                <source>17</source>
                <target>17</target>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-maven-plugin</artifactId>
            <version>0.0.8</version>
            <executions>
                <execution>
                    <id>default-cli</id>
                    <configuration>
                        <mainClass>com.example.latihan2.MainApp</mainClass>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

Thank You

---
