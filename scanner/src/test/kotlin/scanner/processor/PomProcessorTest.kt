package scanner.processor

import io.kotest.core.spec.style.*
import io.kotest.matchers.*
import scanner.testutil.*

class PomProcessorTest : FunSpec({
  val pom = parseXmlFile("presenter-middleware-0.2.10.pom")
  
  test("getDescription") {
    val description = PomProcessor.getDescription(pom)
    description shouldBe "Presenter middleware for updating views based on selectors & reselect for Redux-Kotlin. Mulitiplatform supported."
  }
  
  test("getUrl") {
    val description = PomProcessor.getUrl(pom)
    description shouldBe "https://github.com/reduxkotlin/presenter-middleware/"
  }
  
  test("getScmUrl") {
    val description = PomProcessor.getScmUrl(pom)
    description shouldBe "https://github.com/reduxkotlin/presenter-middleare.git"
  }
})
