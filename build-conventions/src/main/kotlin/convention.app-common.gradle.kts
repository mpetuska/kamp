import ext.AppExtension

plugins {
  id("convention.common")
  kotlin("multiplatform")
}

val app = extensions.create<AppExtension>("app").apply {
}
