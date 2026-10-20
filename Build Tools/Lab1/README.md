# Lab 1: Building and Packaging a Java Application with Maven

Source: https://github.com/Ibrahim-Adel15/CalculatorMavenApp

A simple calculator (sum, subtract, multiply, divide) built, tested and packaged with Maven.

## Project files

| File | Purpose |
|---|---|
| `pom.xml` | Maven build file: Java 17, JUnit 5 dependency, jar named `calculator.jar` with `Main` as entry point |
| `src/main/java/.../Calculator.java` | Calculator logic |
| `src/main/java/.../Main.java` | Reads two numbers and prints the results |
| `src/test/java/.../CalculatorTest.java` | 5 unit tests |

## Steps

### 1. Install Java and Maven

```bash
java -version
mvn -version
```

Used: Temurin JDK 21 and Apache Maven 3.9.9.

### 2. Clone the source code

```bash
git clone https://github.com/Ibrahim-Adel15/CalculatorMavenApp.git
cd CalculatorMavenApp
```

### 3. Run unit tests

```bash
mvn test
```

Result:

```
Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 4. Build the app

```bash
mvn package
ls target/
```

Generates the artifact `target/calculator.jar`.

### 5. Run the app

```bash
java -jar target/calculator.jar
```

### 6. Verify the app is working

Input `10` and `5`:

```
Enter first number: 10
Enter second number: 5
a = 10.0, b = 5.0
Sum: 15.0
Subtract: 5.0
Multiply: 50.0
Divide: 2.0
```
