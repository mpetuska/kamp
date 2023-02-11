package dev.petuska.kodex.cli.cmd.scan.domain

data class FileData<T : Any>(
  val file: RepoFile,
  val data: T,
)
