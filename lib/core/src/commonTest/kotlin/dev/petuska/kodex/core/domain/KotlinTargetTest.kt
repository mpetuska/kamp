package dev.petuska.kodex.core.domain

import dev.petuska.kodex.test.dynamicTests
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import kotlinx.serialization.json.Json
import kotlin.test.Test

class KotlinTargetTest {
  @Test
  fun tests() = dynamicTests {
    (KotlinTarget.values() + KotlinTarget.Unknown("test")).forEach { target ->
      "KotlinTarget ${target::class.simpleName} should be able to handle serialization" {
        val json = Json.encodeToString(KotlinTarget.serializer(), target)
        json shouldBeEqualIgnoringCase """{"id":"${target.id}",""" +
          """"category":"${target.category}",""" +
          """"family":"${target.family}",""" +
          """"platform":"${target.platform}"}"""
        Json.decodeFromString(KotlinTarget.serializer(), json) shouldBe target
      }
    }
  }
}
