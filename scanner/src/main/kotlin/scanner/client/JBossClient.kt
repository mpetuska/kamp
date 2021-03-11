package scanner.client

import io.ktor.client.*
import kamp.domain.*
import kotlinx.serialization.json.*
import org.jsoup.nodes.*
import scanner.domain.*

class JBossClient(
  override val client: HttpClient,
  override val json: Json,
) : MavenRepositoryClient<MavenArtifactImpl>(Repository.J_BOSS.url) {
  override fun parsePage(page: Document): List<String>? = page.getElementsByTag("a")
    ?.mapNotNull { elm ->
      val text = elm.text()
      text.takeIf { it.isNotBlank() && !it.equals("Parent Directory", true) }
    }.also { println(it) }
}
