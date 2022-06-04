package dev.petuska.kamp.core.domain

import dev.petuska.kamp.test.TestFactory
import dev.petuska.kamp.test.dynamicTests
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import kotlinx.serialization.json.Json

class KotlinTargetTest {
  @TestFactory
  fun tests() = dynamicTests {
    (KotlinTarget.values() + KotlinTarget.Unknown("test")).forEach { target ->
      "KotlinTarget ${target::class.simpleName} should be able to handle serialization" {
        val json = Json.encodeToString(KotlinTarget.serializer(), target)
        json shouldBeEqualIgnoringCase
          """{"id":"${target.id}","category":"${target.category}","platform":"${target.platform}"}"""
        Json.decodeFromString(KotlinTarget.serializer(), json) shouldBe target
      }
    }
  }
}
