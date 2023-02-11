package dev.petuska.kodex.cli.cmd.scan.domain

sealed class RepoItem(
  name: String,
) {
  val name: String = name.removeSuffix(SEP).removePrefix(SEP)
  abstract val directory: RepoDirectory

  open val absolutePath: String by lazy {
    "${directory.absolutePath.takeIf { it != SEP } ?: ""}$SEP${this.name}"
  }

  fun url(repositoryRootUrl: String) = "${repositoryRootUrl.removeSuffix(SEP)}$absolutePath"

  override fun toString(): String = absolutePath
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RepoItem) return false

    if (absolutePath != other.absolutePath) return false

    return true
  }

  override fun hashCode(): Int {
    return absolutePath.hashCode()
  }

  companion object {
    const val SEP = "/"
    operator fun invoke(
      name: String,
      directory: RepoDirectory,
    ): RepoItem = if (name.endsWith(SEP)) {
      RepoDirectory(name, directory)
    } else {
      RepoFile(name, directory)
    }
  }
}

class RepoFile(
  name: String,
  override val directory: RepoDirectory,
) : RepoItem(name) {
  fun <T : Any> data(data: T) = FileData(this, data)
}

abstract class RepoDirectory private constructor(
  name: String,
) : RepoItem(name) {
  companion object {
    operator fun invoke(
      name: String,
      directory: RepoDirectory,
    ): RepoDirectory = object : RepoDirectory(name) {
      override val directory: RepoDirectory = directory
    }

    fun fromPath(path: String): RepoDirectory {
      val nPath = path.removePrefix(SEP + SEP).removePrefix(SEP).removeSuffix(SEP)
      return if (nPath == SEP || nPath.isBlank()) {
        Root
      } else if (!nPath.contains(SEP)) {
        RepoDirectory(nPath, Root)
      } else {
        val chunks = nPath.split(SEP)
        val name = chunks.last()
        val parentPath = chunks.dropLast(1).joinToString(SEP)
        RepoDirectory(name, fromPath(parentPath))
      }
    }
  }

  fun list(items: Collection<RepoItem>): Listed = Listed(name, this, items)

  fun dir(name: String) = RepoDirectory(name, this)
  fun file(name: String) = RepoFile(name, this)
  fun item(name: String) = RepoItem.invoke(name, this)

  object Root : RepoDirectory("") {
    override val directory: RepoDirectory get() = this
    override val absolutePath: String = SEP
  }

  class Listed(
    name: String,
    override val directory: RepoDirectory,
    val items: Collection<RepoItem>
  ) : RepoDirectory(name)
}
