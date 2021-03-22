package scanner.processor

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import scanner.testutil.*

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
