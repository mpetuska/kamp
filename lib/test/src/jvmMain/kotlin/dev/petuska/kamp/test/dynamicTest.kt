package dev.petuska.kamp.test

import org.junit.jupiter.api.DynamicTest

@Suppress("ACTUAL_WITHOUT_EXPECT", "ACTUAL_TYPE_ALIAS_TO_CLASS_WITH_DECLARATION_SITE_VARIANCE")
actual typealias DynamicTests = Collection<DynamicTest>

actual fun dynamicTests(builder: DynamicTestBuilder.() -> Unit): DynamicTests {
  val tests = DynamicTestBuilder().apply(builder).tests
  return tests.map { (name, test) ->
    DynamicTest.dynamicTest(name, test)
  }
}
