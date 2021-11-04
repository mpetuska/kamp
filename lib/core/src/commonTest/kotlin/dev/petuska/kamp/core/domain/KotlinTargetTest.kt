package dev.petuska.kamp.core.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import kotlinx.serialization.json.Json

class KotlinTargetTest : StringSpec({
  KotlinTarget.values().forEach { target ->
    "KotlinTarget ${target::class.simpleName} should be able to handle serialization" {
      val json = Json.encodeToString(KotlinTarget.serializer(), target)
      json shouldBeEqualIgnoringCase
          """{"category":"${target.category}","platform":"${target.platform}"}"""
      Json.decodeFromString(KotlinTarget.serializer(), json) shouldBe target
    }
  }
})
