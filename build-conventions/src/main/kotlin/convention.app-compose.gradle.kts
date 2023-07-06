// import ext.AppExtension
// import ext.JvmAppExtension
//
// plugins {
//  id("convention.app-mpp")
//  id("convention.compose")
// }
//
// extensions.configure<AppExtension> {
//  the<JvmAppExtension>().fatJar.set(false)
// }
//
// kotlin {
//  sourceSets {
//    commonMain {
//      dependencies {
//        implementation(compose.runtime)
//      }
//    }
//    jsMain {
//      dependencies {
//        implementation(compose.web.core)
//        implementation(compose.web.svg)
//      }
//    }
//    jsTest {
//      dependencies {
//        implementation(compose.web.testUtils)
//      }
//      languageSettings {
//        optIn("org.jetbrains.compose.web.testutils.ComposeWebExperimentalTestsApi")
//      }
//    }
//    androidMain {
//      dependencies {
//        implementation("androidx.activity:activity-compose:_")
//        implementation("androidx.appcompat:appcompat:_")
//        implementation("androidx.core:core-ktx:_")
//      }
//    }
//    jvmMain {
//      dependencies {
//        implementation(compose.desktop.currentOs)
//      }
//    }
//    jvmCommonMain {
//      dependencies {
//        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
//        implementation(compose.material3)
//      }
//    }
//    jvmCommonInstrumentedTest {
//      dependencies {
//        @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
//        implementation(compose.uiTestJUnit4)
//      }
//    }
//  }
// }
