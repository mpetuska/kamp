package dev.petuska.kamp.core.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import kotlin.test.assertEquals
import kotlinx.serialization.json.Json

class KotlinTargetTest : FunSpec({
  KotlinTarget.values().forEach { target ->
    test("KotlinTarget ${target::class.simpleName} should be able to handle serialization") {
      val json = Json.encodeToString(KotlinTarget.serializer(), target)
      json shouldBeEqualIgnoringCase
          """{"category":"${target.category}","platform":"${target.platform}"}"""
      assertEquals(target, Json.decodeFromString(KotlinTarget.serializer(), json))
    }
  }
})
