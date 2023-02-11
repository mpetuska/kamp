package dev.petuska.kodex.cli.cmd.scan.processor

import dev.petuska.kodex.cli.testutil.parseXmlFile
import dev.petuska.kodex.test.dynamicTests
import io.kotest.matchers.shouldBe
import org.junit.Test

class PomProcessorTest {
  @Test
  fun tests() = dynamicTests {
    val pom = parseXmlFile("presenter-middleware-0.2.10.pom")
    dynamicTest("getDescription") {
      with(PomProcessor()) {
        val description = pom.description
        description shouldBe
          "Presenter middleware for updating views based on selectors & " +
          "reselect for Redux-Kotlin. Mulitiplatform supported."
      }
    }
    dynamicTest("getUrl") {
      with(PomProcessor()) {
        val description = pom.url
        description shouldBe "https://github.com/reduxkotlin/presenter-middleware/"
      }
    }
    dynamicTest("getScmUrl") {
      with(PomProcessor()) {
        val description = pom.scmUrl
        description shouldBe "https://github.com/reduxkotlin/presenter-middleare.git"
      }
    }
  }
}
