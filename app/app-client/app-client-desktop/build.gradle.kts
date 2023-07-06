plugins {
  id("convention.app-jvm")
  id("convention.compose")
}

app {
  jvm {
    mainClass.set("dev.petuska.kodex.client.MainKt")
  }
}
