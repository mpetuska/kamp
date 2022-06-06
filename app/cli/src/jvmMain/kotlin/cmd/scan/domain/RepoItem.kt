package dev.petuska.kamp.cli.cmd.scan.domain

sealed class RepoItem(
  val repositoryRootUrl: String,
  name: String,
) {
  val name: String = name.removeSuffix(SEP).removePrefix(SEP)
  abstract val directory: RepoDirectory

  open val absolutePath: String by lazy {
    "${directory.absolutePath.takeIf { it != SEP } ?: ""}$SEP${this.name}"
  }
  val url by lazy { "${repositoryRootUrl.removeSuffix(SEP)}$absolutePath" }

  override fun toString(): String = absolutePath
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is RepoItem) return false

    if (url != other.url) return false

    return true
  }

  override fun hashCode(): Int {
    return url.hashCode()
  }

  companion object {
    const val SEP = "/"
    operator fun invoke(
      repositoryRootUrl: String,
      name: String,
      directory: RepoDirectory,
    ): RepoItem = if (name.endsWith(SEP)) {
      RepoDirectory(repositoryRootUrl, name, directory)
    } else {
      RepoFile(repositoryRootUrl, name, directory)
    }
  }
}

class RepoFile(
  repositoryRootUrl: String,
  name: String,
  override val directory: RepoDirectory,
) : RepoItem(repositoryRootUrl, name) {
  fun <T : Any> data(data: T) = FileData(this, data)
}

abstract class RepoDirectory private constructor(
  repositoryRootUrl: String,
  name: String,
) : RepoItem(repositoryRootUrl, name) {
  companion object {
    operator fun invoke(
      repositoryRootUrl: String,
      name: String,
      directory: RepoDirectory,
    ): RepoDirectory = object : RepoDirectory(repositoryRootUrl, name) {
      override val directory: RepoDirectory = directory
    }

    fun fromPath(repositoryRootUrl: String, path: String): RepoDirectory {
      val nPath = path.removePrefix(SEP + SEP).removePrefix(SEP).removeSuffix(SEP)
      return if (nPath == SEP || nPath.isBlank()) {
        Root(repositoryRootUrl)
      } else if (!nPath.contains(SEP)) {
        RepoDirectory(repositoryRootUrl, nPath, Root(repositoryRootUrl))
      } else {
        val chunks = nPath.split(SEP)
        val name = chunks.last()
        val parentPath = chunks.dropLast(1).joinToString(SEP)
        RepoDirectory(repositoryRootUrl, name, fromPath(repositoryRootUrl, parentPath))
      }
    }
  }

  fun list(items: Collection<RepoItem>): Listed = Listed(repositoryRootUrl, name, this, items)

  fun dir(name: String) = RepoDirectory(repositoryRootUrl, name, this)
  fun file(name: String) = RepoFile(repositoryRootUrl, name, this)
  fun item(name: String) = RepoItem.invoke(repositoryRootUrl, name, this)

  class Root(repositoryRootUrl: String) : RepoDirectory(repositoryRootUrl, "") {
    override val directory: RepoDirectory get() = this
    override val absolutePath: String = SEP
  }

  class Listed(
    repositoryRootUrl: String,
    name: String,
    override val directory: RepoDirectory,
    val items: Collection<RepoItem>
  ) : RepoDirectory(repositoryRootUrl, name)
}
