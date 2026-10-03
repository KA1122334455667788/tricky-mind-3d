name: Build Tricky Mind 3D APK

on:
  workflow_dispatch:
  push:
    branches:
      - main

jobs:
  build-apk:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout project
        uses: actions/checkout@v4

      - name: Set up JDK 17
        uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '17'

      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v4
        with:
          gradle-version: '8.9'

      - name: Verify Gradle project
        run: |
          echo "=== CURRENT DIRECTORY ==="
          pwd
          echo "=== ROOT FILES ==="
          ls -la

          echo "=== GRADLE FILES ==="
          test -f settings.gradle.kts && echo "settings.gradle.kts FOUND"
          test -f build.gradle.kts && echo "build.gradle.kts FOUND"
          test -f gradle.properties && echo "gradle.properties FOUND"

      - name: Verify Android module
        run: |
          echo "=== APP DIRECTORY ==="
          ls -la app
          test -f app/build.gradle.kts && echo "app/build.gradle.kts FOUND"

          echo "=== MANIFEST ==="
          test -f app/src/main/AndroidManifest.xml && echo "app/src/main/AndroidManifest.xml FOUND"

          echo "=== SOURCE ==="
          ls -la app/src/main

      - name: Verify Gradle modules
        run: |
          gradle projects --stacktrace

      - name: Build Debug APK
        run: |
          gradle :app:assembleDebug --stacktrace

      - name: Upload APK
        uses: actions/upload-artifact@v4
        with:
          name: tricky-mind-3d-debug-apk
          path: app/build/outputs/apk/debug/app-debug.apk
