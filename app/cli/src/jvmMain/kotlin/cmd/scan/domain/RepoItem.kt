package dev.petuska.kamp.cli.cmd.scan.domain

sealed class RepoItem(
  val repositoryRootUrl: String,
  name: String,
) {
  val name: String = name.removeSuffix(SEP)
  abstract val directory: RepoDirectory

  open val absolutePath: String = "${directory.absolutePath.takeIf { it != SEP } ?: ""}$SEP$name".removeSuffix(SEP)
  val url = "$repositoryRootUrl$absolutePath"

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
      directoryPath: String,
    ): RepoItem = if (name.endsWith(SEP)) {
      RepoDirectory.fromPath(repositoryRootUrl, "$directoryPath$SEP$name")
    } else {
      RepoFile(repositoryRootUrl, name, RepoDirectory.fromPath(repositoryRootUrl, directoryPath))
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

open class RepoDirectory private constructor(
  tmpDir: RepoDirectory?,
  repositoryRootUrl: String,
  name: String,
) : RepoItem(repositoryRootUrl, name) {
  constructor (
    repositoryRootUrl: String,
    name: String,
    directory: RepoDirectory
  ) : this(directory, repositoryRootUrl, name)

  override val directory: RepoDirectory by lazy {
    tmpDir!!
  }

  companion object {
    fun fromPath(repositoryRootUrl: String, path: String): RepoDirectory {
      val nPath = path.removePrefix(SEP + SEP).removePrefix(SEP).removeSuffix(SEP)
      return if (nPath == SEP || nPath.isBlank()) {
        Root(repositoryRootUrl)
      } else if (!nPath.contains(SEP)) {
        RepoDirectory(repositoryRootUrl, path, Root(repositoryRootUrl))
      } else {
        val chunks = path.split(SEP)
        val name = chunks.last()
        val parentPath = chunks.dropLast(1).joinToString(SEP)
        RepoDirectory(repositoryRootUrl, name, fromPath(repositoryRootUrl, parentPath))
      }
    }
  }

  fun list(items: Collection<RepoItem>): Listed =
    Listed(repositoryRootUrl, name, directory, items)

  fun dir(name: String) = RepoDirectory(repositoryRootUrl, name, directory)
  fun file(name: String) = RepoFile(repositoryRootUrl, name, directory)
  fun item(name: String) = invoke(repositoryRootUrl, name, directory.absolutePath)

  class Root(repositoryRootUrl: String) : RepoDirectory(null, repositoryRootUrl, "") {
    override val directory: RepoDirectory get() = this
    override val absolutePath: String = SEP
  }

  class Listed(
    repositoryRootUrl: String,
    name: String,
    directory: RepoDirectory,
    val items: Collection<RepoItem>
  ) : RepoDirectory(repositoryRootUrl, name, directory)
}
