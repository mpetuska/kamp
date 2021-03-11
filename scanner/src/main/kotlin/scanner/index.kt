package scanner

import kotlinx.cli.*
import scanner.config.*
import scanner.service.*


suspend fun main(args: Array<String>) {
  val parser = ArgParser("scanner")
  val scanner by parser.argument(
    type = ArgType.Choice(listOf("mavenCentral"), { it }),
    description = "Repository alias to scan for"
  )
  
  val from by parser.option(
    type = ArgType.Choice(('a'..'z').toList(), { it[0] }),
    shortName = "f",
    description = "Repository root page filter start"
  )
  val to by parser.option(
    type = ArgType.Choice(('a'..'z').toList(), { it[0] }),
    shortName = "t",
    description = "Repository root page filter end"
  )
  val include by parser.option(
    type = ArgType.String,
    shortName = "i",
    description = "Repository root page filter to include"
  ).multiple()
  parser.parse(args)
  
  val rangeFilter = from?.let { f -> to?.let { t -> (f..t).map(Char::toString).toSet() } } ?: setOf()
  val filters = ((include.toSet()) + rangeFilter).takeIf { it.isNotEmpty() }
  
  Orchestrator(di).run(scanner, filters)
}
