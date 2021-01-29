plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("com.microsoft.azure.azurefunctions") version Version.azureFuntionsPlugin
}

kotlin {
  sourceSets {
    main {
      dependencies {
        implementation(project(rootProject.path))
//        implementation("io.ktor:ktor-server-cio:${Version.ktor}")
        implementation("com.microsoft.azure.functions:azure-functions-java-library:${Version.azureFuntionsLibrary}")
        implementation("org.slf4j:slf4j-simple:1.7.30")
//        implementation("ch.qos.logback:logback-classic:1.2.3")
//        implementation("org.litote.kmongo:kmongo-coroutine-serialization:${Version.kmongo}")
      }
    }
    test {
      dependencies {
        implementation("io.kotest:kotest-runner-junit5:4.3.0")
      }
    }
  }
}

azurefunctions {
  resourceGroup = rootProject.name
  appName = rootProject.name
  appServicePlanName = rootProject.name
  
  pricingTier = "Free"
  isDisableAppInsights = true
  localDebug = "transport=dt_socket,server=y,suspend=n,address=5005"
}
