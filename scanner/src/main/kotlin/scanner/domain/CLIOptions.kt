package scanner.domain

data class CLIOptions(
  val scanner: String,
  val include: Set<String>?,
  val exclude: Set<String>?,
  val delayMS: Long?,
  val workers: Int?,
) {
  constructor(
    scanner: String,
    include: Set<String>?,
    from: Char?,
    to: Char?,
    exclude: Set<String>?,
    delayMS: Long?,
    workers: Int?,
  ) : this(scanner, ((include ?: setOf()) + (from?.let { to?.let { (from..to).toSet() } } ?: setOf()).map(Char::toString)).takeIf { it.isNotEmpty() }, exclude, delayMS, workers)
}
