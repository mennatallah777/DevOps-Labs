# Lab 2: Building and Packaging a Java Application with Gradle

Source: https://github.com/Ibrahim-Adel15/CalculatorGradleApp

The same calculator app as Lab 1, built with Gradle instead of Maven.

## Project files

| File | Purpose |
|---|---|
| `build.gradle` | Gradle build file: `java` + `application` plugins, Java 17 toolchain, JUnit 5, jar named `calculator.jar` |
| `settings.gradle` | Project name (`calculator`) |
| `src/main/java/...` | Calculator logic and `Main` |
| `src/test/java/...` | 5 unit tests |

## Steps

### 1. Install Gradle

The build requires a Java 17 toolchain, so Gradle is run from the official Docker image that bundles Gradle 8.10.2 with JDK 17:

```bash
docker pull gradle:8.10.2-jdk17
docker run --rm gradle:8.10.2-jdk17 gradle -v
```

Optional shortcut so `gradle` can be typed directly:

```bash
alias gradle='docker run --rm -u $(id -u):$(id -g) -e GRADLE_USER_HOME=/work/.gradle -v $PWD:/work -w /work gradle:8.10.2-jdk17 gradle'
```

### 2. Clone the source code

```bash
git clone https://github.com/Ibrahim-Adel15/CalculatorGradleApp.git
cd CalculatorGradleApp
```

### 3. Run unit tests

```bash
gradle test
```

Result: 5 tests, 0 failures.

### 4. Build the app

```bash
gradle build
ls build/libs/
```

Generates the artifact `build/libs/calculator.jar`.

### 5. Run the app

```bash
java -jar build/libs/calculator.jar
```

### 6. Verify the app is working

Input `10` and `5`:

```
Sum: 15.0
Subtract: 5.0
Multiply: 50.0
Divide: 2.0
```

## Maven vs Gradle

| | Maven (Lab 1) | Gradle (Lab 2) |
|---|---|---|
| Build file | `pom.xml` (XML) | `build.gradle` (Groovy script) |
| Test | `mvn test` | `gradle test` |
| Build | `mvn package` | `gradle build` |
| Output | `target/calculator.jar` | `build/libs/calculator.jar` |
