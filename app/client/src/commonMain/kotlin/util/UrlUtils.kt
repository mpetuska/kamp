package app.client.util

import app.client.config.AppEnv

class UrlUtils(private val env: AppEnv) {
  fun String.toApiUrl() = "${env.API_URL}/${removePrefix("/")}"
}

private val urlParameterRegex = Regex("""\{(.+)\}""")
fun buildUrl(url: String, vararg parameters: Pair<String, Any?>): String {
  val requiredParameters = urlParameterRegex.findAll(url)
    .map { it.groups[1]?.value ?: error("${it.value} doesn't conform to url param schema") }

  val paramMap = parameters.toMap()
  return (sequenceOf(url) + requiredParameters).reduce { acc, parameter ->
    val value = paramMap[parameter] ?: error("Value for parameter $parameter not supplied")
    acc.replace("{$parameter}", value.toString())
  }
}
