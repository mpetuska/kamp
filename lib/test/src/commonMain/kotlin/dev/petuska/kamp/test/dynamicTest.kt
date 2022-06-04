package dev.petuska.kamp.test

expect class DynamicTest

expect fun dynamicTests(builder: DynamicTestBuilder.() -> Unit): Collection<DynamicTest>

class DynamicTestBuilder {
  internal val tests = mutableMapOf<String, () -> Unit>()

  fun dynamicTest(name: String, test: () -> Unit) {
    tests[name] = test
  }

  operator fun String.invoke(test: () -> Unit) = dynamicTest(this, test)
}
