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
