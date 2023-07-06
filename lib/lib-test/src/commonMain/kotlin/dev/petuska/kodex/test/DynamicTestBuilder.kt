package dev.petuska.kodex.test

import io.kotest.matchers.shouldBe

fun dynamicTests(builder: DynamicTestBuilder.() -> Unit) {
  val tests = DynamicTestBuilder().apply(builder).tests
  val results = tests.map { (name, test) ->
    print("[TEST] $name")
    val scope = DynamicTestBuilder.DynamicTestScope()
    runCatching {
      scope.test()
    }.also {
      if (it.isSuccess) {
        println(" [PASS]")
        scope.printAll()
      } else {
        println(" [FAIL]")
        scope.printAll()
        it.exceptionOrNull()?.printStackTrace()
      }
    }
  }
  results.none { it.isFailure } shouldBe true
}

class DynamicTestBuilder {
  internal val tests = mutableMapOf<String, DynamicTestScope.() -> Unit>()

  class DynamicTestScope {
    private val logs = mutableListOf<() -> Unit>()

    internal fun printAll() {
      logs.forEach { it() }
    }

    fun print(any: Any?) {
      logs + { kotlin.io.print(any) }
    }

    fun println(any: Any? = null) {
      logs + { kotlin.io.println(any) }
    }
  }

  fun dynamicTest(name: String, test: DynamicTestScope.() -> Unit) {
    tests[name] = test
  }

  operator fun String.invoke(test: DynamicTestScope.() -> Unit) = dynamicTest(this, test)
}
