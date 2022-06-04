package dev.petuska.kamp.test

import io.kotest.matchers.shouldBe

@Suppress("ACTUAL_WITHOUT_EXPECT")
actual typealias DynamicTests = Unit

actual fun dynamicTests(builder: DynamicTestBuilder.() -> Unit) {
  val tests = DynamicTestBuilder().apply(builder).tests
  val results = tests.map { (name, test) ->
    println("[TEST] $name")
    runCatching {
      test()
    }.onFailure { it.printStackTrace() }
  }
  results.none { it.isFailure } shouldBe true
  return
}
