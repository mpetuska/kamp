package app.service

import app.domain.*
import app.util.*
import io.ktor.application.*
import kamp.domain.*

actual class LibraryService(private val call: ApplicationCall) {
  actual suspend fun getAll(page: Int, size: Int): PagedResponse<KotlinMPPLibrary> {
    return PagedResponse(
      data = listOf(
        KotlinMPPLibrary("lt.petuska",
          "kamp",
          "1.0.0",
          setOf(KotlinTarget.Common(),
            KotlinTarget.JS.IR(),
            KotlinTarget.JS.Legacy(),
            KotlinTarget.JVM.Java(),
            KotlinTarget.JVM.Android(),
            KotlinTarget.Native("mingwX64")),
          """
            Mock library purely hard-coded for kamp website testing purposes. Not an actual library, so do not get confused.
          """.trimIndent(),
          "https://kamp.ml",
          "https://github.com/mpetuska/kamp.git")
      ),
      total = 11,
      next = call.request.buildNextUrl(10),
      prev = call.request.buildPrevUrl()
    )
  }
}
