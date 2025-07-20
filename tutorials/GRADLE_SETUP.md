# Cross-platform Gradle Configuration

This project supports development on both Windows and macOS.

## Default Configuration (Recommended)

The main `gradle.properties` file is configured to work on both platforms by letting Gradle auto-detect your Java installation. This should work in most cases.

## Platform-Specific Configuration (If Needed)

If you encounter Java detection issues, use the platform-specific templates:

### For Windows

1. Copy `gradle-windows.properties` to `gradle.properties`
2. Uncomment and update the Java path that matches your installation

### For macOS  

1. Copy `gradle-macos.properties` to `gradle.properties`
2. Uncomment and update the Java path that matches your installation

## Finding Your Java Installation

### Windows

```cmd
where java
echo %JAVA_HOME%
```

### macOS

```bash
which java
echo $JAVA_HOME
/usr/libexec/java_home -V
```

## Common Java Installation Paths

### Windows

- Oracle JDK: `C:/Program Files/Java/jdk-21`
- Eclipse Temurin: `C:/Program Files/Eclipse Adoptium/jdk-21.0.1.12-hotspot/`
- OpenJDK: `C:/Program Files/Microsoft/jdk-21.0.1.12-hotspot/`

### macOS

- Homebrew: `/opt/homebrew/opt/openjdk@21` (Apple Silicon) or `/usr/local/opt/openjdk@21` (Intel)
- Oracle/Temurin: `/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home`
- SDKMAN: `~/.sdkman/candidates/java/21.0.1-tem`

## Gradle Wrapper

This project includes Gradle Wrapper, so you can use:

- Windows: `gradlew.bat build`
- macOS/Linux: `./gradlew build`
