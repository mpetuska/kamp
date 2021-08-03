package app.client.util

import app.client.config.AppEnv

class UrlUtils(private val env: AppEnv) {
  fun String.toApiUrl() = "${env.API_URL}/${removePrefix("/")}"
}
