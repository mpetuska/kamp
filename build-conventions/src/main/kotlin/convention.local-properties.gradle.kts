import java.util.*


fun loadLocalProperties(file: File) {
  file.takeIf(File::exists)?.let { f ->
    Properties().apply {
      f.inputStream().use(::load)
    }.mapKeys { (k, _) -> k.toString() }
  }?.toList()?.forEach { (k, v) ->
    project.extra[k] = v
  }
}

loadLocalProperties(rootDir.resolve("local.properties"))
loadLocalProperties(projectDir.resolve("local.properties"))
