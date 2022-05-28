package dev.petuska.kamp.cli.processor

import dev.petuska.kamp.cli.testutil.parseXmlFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PomProcessorTest :
  FunSpec({
    val pom = parseXmlFile("presenter-middleware-0.2.10.pom")

    test("getDescription") {
      with(PomProcessor()) {
        val description = pom.description
        description shouldBe
          "Presenter middleware for updating views based on selectors & reselect for Redux-Kotlin. Mulitiplatform supported."
      }
    }

    test("getUrl") {
      with(PomProcessor()) {
        val description = pom.url
        description shouldBe "https://github.com/reduxkotlin/presenter-middleware/"
      }
    }

    test("getScmUrl") {
      with(PomProcessor()) {
        val description = pom.scmUrl
        description shouldBe "https://github.com/reduxkotlin/presenter-middleare.git"
      }
    }
  })
