package dev.petuska.kamp.test

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect class DynamicTests

expect fun dynamicTests(builder: DynamicTestBuilder.() -> Unit): DynamicTests

class DynamicTestBuilder {
  internal val tests = mutableMapOf<String, () -> Unit>()

  fun dynamicTest(name: String, test: () -> Unit) {
    tests[name] = test
  }

  operator fun String.invoke(test: () -> Unit) = dynamicTest(this, test)
}
