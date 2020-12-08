package scanner.client

import io.kotest.core.spec.style.*
import io.kotest.matchers.collections.*

class JCenterClientTest : MavenRepositoryClientTest(JCenterClient)

class MavenCentralClientTest : MavenRepositoryClientTest(MavenCentralClient)

abstract class MavenRepositoryClientTest(target: MavenRepositoryClient<*>) : FunSpec({
  test("getLatestVersion") { }
  
  test("getGradleModule") { }
  
  test("getMavenPom") {
  }
  
  xtest("listRepositoryPath") {
    suspend fun assertPage(path: String, vararg elm: String) {
      val list = target.listRepositoryPath(path)
      list shouldContainAll elm.asList()
    }
  
    assertPage("/io/ktor", "ktor-client-cio/")
    assertPage("/io/ktor/ktor-client-cio", "1.4.0/", "maven-metadata.xml")
    assertPage("/io/ktor/ktor-client-cio/1.4.0", "ktor-client-cio-1.4.0.module", "ktor-client-cio-1.4.0.pom")
  }
})
