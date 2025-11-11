// build.gradle.kts (Project)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // If you’re using the Hilt plugin alias, declare it here too:
    alias(libs.plugins.hilt.android.plugin) apply false
    // ❌ Do NOT put kotlin-kapt here
    id("androidx.navigation.safeargs.kotlin") version "2.8.3" apply false


}
