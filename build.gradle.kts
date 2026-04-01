plugins {
    // JetBrains Compose 1.5.12 ile Kotlin 1.9.20 en uyumlu kombinasyondur.
    kotlin("multiplatform") version "1.9.20" apply false
    kotlin("android") version "1.9.20" apply false
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.compose") version "1.5.12" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
