package dev.petuska.kamp.core.domain

import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import kotlinx.serialization.json.Json
import kotlin.test.Test

class KotlinTargetTest {
  @Test
  fun tests() {
    val results = KotlinTarget.values().map { target ->
      println("[TEST] KotlinTarget ${target::class.simpleName} should be able to handle serialization")
      runCatching {
        val json = Json.encodeToString(KotlinTarget.serializer(), target)
        json shouldBeEqualIgnoringCase
          """{"category":"${target.category}","platform":"${target.platform}"}"""
        Json.decodeFromString(KotlinTarget.serializer(), json) shouldBe target
      }.onFailure { it.printStackTrace() }
    }
    results.none { it.isFailure } shouldBe true
  }
}
