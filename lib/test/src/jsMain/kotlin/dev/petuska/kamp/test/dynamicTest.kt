package dev.petuska.kamp.test

import io.kotest.matchers.shouldBe

actual typealias DynamicTest = Any

actual fun dynamicTests(builder: DynamicTestBuilder.() -> Unit): dynamic {
  val tests = DynamicTestBuilder().apply(builder).tests
  val results = tests.map { (name, test) ->
    println("[TEST] $name")
    runCatching {
      test()
    }.onFailure { it.printStackTrace() }
  }
  results.none { it.isFailure } shouldBe true
  return Unit
}
