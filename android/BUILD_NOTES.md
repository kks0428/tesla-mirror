# Build notes

## Current compatibility choice
- Android Gradle Plugin: 8.8.0
- Kotlin: 2.1.10
- Compose compiler plugin: org.jetbrains.kotlin.plugin.compose 2.1.10

## Why
Kotlin 2.x requires the Compose compiler Gradle plugin instead of only using `composeOptions.kotlinCompilerExtensionVersion`.
