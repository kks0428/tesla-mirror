# JDK notes

If Gradle toolchain detection fails on this host, force it with:

- org.gradle.java.installations.auto-detect=false
- org.gradle.java.installations.paths=/usr/lib/jvm/java-21-openjdk-arm64

If the error still persists, verify that `javac` exists under that path and consider exporting JAVA_HOME explicitly before build.
