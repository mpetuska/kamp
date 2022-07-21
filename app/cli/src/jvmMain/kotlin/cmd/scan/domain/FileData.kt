package dev.petuska.kamp.cli.cmd.scan.domain

data class FileData<T : Any>(
  val file: RepoFile,
  val data: T,
)
