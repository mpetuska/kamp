package scanner

import scanner.config.*
import scanner.service.*


suspend fun main(args: Array<String>) = Orchestrator(di).run(args)
