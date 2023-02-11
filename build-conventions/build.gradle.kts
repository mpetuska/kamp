@file:Suppress("OPT_IN_IS_NOT_ENABLED")

import de.fayard.refreshVersions.core.RefreshVersionsCorePlugin
import de.fayard.refreshVersions.core.internal.InternalRefreshVersionsApi
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
  mavenCentral()
  google()
  if (findProperty("project.enableSnapshots") == "true") {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

dependencies {
  implementation("com.android.tools.build:gradle:_")
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
  implementation("org.jetbrains.kotlin:kotlin-serialization:_")
  implementation("org.jetbrains.compose:compose-gradle-plugin:_")
  implementation("com.github.jakemarsden:git-hooks-gradle-plugin:_")
  implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:_")
  implementation("gradle.plugin.com.github.jengelman.gradle.plugins:shadow:_")
  implementation("dev.petuska:klip-gradle-plugin:_")
  @OptIn(InternalRefreshVersionsApi::class)
  implementation(
    "de.fayard.refreshVersions:refreshVersions-core:${RefreshVersionsCorePlugin.currentVersion}"
  )
}

kotlin {
  sourceSets {
    all {
      languageSettings.optIn("kotlin.RequiresOptIn")
    }
  }
}

tasks {
  withType<KotlinCompile> {
    kotlinOptions {
      languageVersion = "1.4" // 1.9 from gradle 8
    }
  }
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}
