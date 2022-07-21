plugins {
  if (System.getenv("CI") == null) id("convention.git-hooks")
  id("convention.common")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"
  }
}

tasks {
  register("detektAll", io.gitlab.arturbosch.detekt.Detekt::class) {
    description = "Run Detekt for all modules"
    config.from(project.detekt.config)
    buildUponDefaultConfig = project.detekt.buildUponDefaultConfig
    setSource(files(projectDir))
  }
}
