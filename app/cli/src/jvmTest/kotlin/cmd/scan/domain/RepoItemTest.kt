package dev.petuska.kamp.cli.cmd.scan.domain

import dev.petuska.kamp.cli.cmd.scan.domain.RepoItem.Companion.SEP
import dev.petuska.kamp.test.TestFactory
import dev.petuska.kamp.test.dynamicTests
import io.kotest.matchers.shouldBe

class RepoItemTest {
  @TestFactory
  fun directory() = dynamicTests {
    listOf(
      "/", "/dir", "/dir/sub/"
    ).forEach { path ->
      "[$path] should be parsed into RepoDirectory" {
        val dir = RepoDirectory.fromPath(path)
        dir.absolutePath shouldBe if (path.length <= 1) SEP else path.removeSuffix(SEP)
      }
    }
  }

  @TestFactory
  fun directoryChildren() = dynamicTests {
    val parents = listOf("/", "/dir", "/dir/sub/").map(RepoDirectory::fromPath)
    val children = listOf(
      "",
      "/",
      "child",
      "/child",
      "/child/",
    )
    parents.forEach { parent ->
      children.forEach { child ->
        "Parent [$parent] should be able to nest child [$child]" {
          val next = parent.item(child)
          val cName = child.removePrefix(SEP).removeSuffix(SEP)
          next.absolutePath shouldBe "${parent.absolutePath.takeIf { it != SEP } ?: ""}$SEP$cName"
        }
      }
    }
  }

  @TestFactory
  fun item() = dynamicTests {
    val parent = RepoDirectory.Root
    "/dev/petuska/gradle-kotlin-delegates/maven-metadata.xml" {
      val next = parent.item("/dev/petuska/gradle-kotlin-delegates/maven-metadata.xml")
      (next is RepoFile) shouldBe true
    }
  }
}
