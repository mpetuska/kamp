package app.client.util

import app.client.config.env

fun String.toApi() = "${env.API_URL}/${this.removePrefix("/")}"
