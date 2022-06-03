package dev.petuska.kamp.cli.cmd.scan.domain

sealed class RepositoryItem(
  val name: String,
  directoryPath: String,
) {
  val parentPath = "/${directoryPath.removeSuffix("/").removePrefix("/")}"
  val path = "${if (parentPath == "/") "" else parentPath}/$name".removeSuffix("/")

  override fun toString(): String = path

  companion object {
    operator fun invoke(
      name: String,
      directoryPath: String,
    ): RepositoryItem = if (name.endsWith("/")) {
      Directory(name, directoryPath)
    } else {
      File(name, directoryPath)
    }
  }

  class File(value: String, path: String) : RepositoryItem(value, path)

  class Directory(value: String, path: String) : RepositoryItem(value, path)
}
