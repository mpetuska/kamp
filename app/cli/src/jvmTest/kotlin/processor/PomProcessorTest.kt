package dev.petuska.kamp.cli.processor

import dev.petuska.kamp.cli.cmd.scan.processor.PomProcessor
import dev.petuska.kamp.cli.testutil.parseXmlFile
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory

class PomProcessorTest {
  @TestFactory
  fun tests(): Array<DynamicTest> {
    val pom = parseXmlFile("presenter-middleware-0.2.10.pom")
    return arrayOf(
      dynamicTest("getDescription") {
        with(PomProcessor()) {
          val description = pom.description
          description shouldBe
            "Presenter middleware for updating views based on selectors & " +
            "reselect for Redux-Kotlin. Mulitiplatform supported."
        }
      },
      dynamicTest("getUrl") {
        with(PomProcessor()) {
          val description = pom.url
          description shouldBe "https://github.com/reduxkotlin/presenter-middleware/"
        }
      },
      dynamicTest("getScmUrl") {
        with(PomProcessor()) {
          val description = pom.scmUrl
          description shouldBe "https://github.com/reduxkotlin/presenter-middleare.git"
        }
      }
    )
  }
}
