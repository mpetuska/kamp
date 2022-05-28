package dev.petuska.kamp.cli

import dev.petuska.kamp.cli.config.di
import dev.petuska.kamp.cli.domain.CLIOptions
import dev.petuska.kamp.cli.domain.Repository
import dev.petuska.kamp.cli.service.Orchestrator
import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.multiple

suspend fun main(args: Array<String>) {
  val parser = ArgParser("cli")
  val scanner by
  parser.argument(
    type = ArgType.Choice(Repository.values().map(Repository::alias), { it }),
    description = "Repository alias to scan for"
  )

  val from by
  parser.option(
    type = ArgType.Choice(('a'..'z').toList(), { it[0] }),
    shortName = "f",
    description = "Repository root page filter start"
  )
  val to by
  parser.option(
    type = ArgType.Choice(('a'..'z').toList(), { it[0] }),
    shortName = "t",
    description = "Repository root page filter end"
  )
  val include by
  parser
    .option(
      type = ArgType.String,
      shortName = "i",
      description = "Repository root page filter to include"
    )
    .multiple()
  val exclude by
  parser
    .option(
      type = ArgType.String,
      shortName = "e",
      description = "Repository root page filter to exclude"
    )
    .multiple()
  val delay by
  parser.option(
    type = ArgType.Int,
    shortName = "d",
    description = "Worker processing delay in milliseconds"
  )
  val workers by
  parser.option(type = ArgType.Int, shortName = "w", description = "Concurrent worker count")
  parser.parse(args)
  val cliOptions =
    CLIOptions(
      scanner = scanner,
      include = include.toSet().takeIf { it.isNotEmpty() },
      from = from,
      to = to,
      exclude = exclude.toSet().takeIf { it.isNotEmpty() },
      delayMS = delay?.toLong(),
      workers = workers
    )

  Orchestrator(di).run(scanner, cliOptions)
}
