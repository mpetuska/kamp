package dev.petuska.kamp.test

import org.junit.jupiter.api.DynamicTest

actual typealias DynamicTest = DynamicTest

actual fun dynamicTests(builder: DynamicTestBuilder.() -> Unit): Collection<DynamicTest> {
  val tests = DynamicTestBuilder().apply(builder).tests
  return tests.map { (name, test) ->
    DynamicTest.dynamicTest(name, test)
  }
}
