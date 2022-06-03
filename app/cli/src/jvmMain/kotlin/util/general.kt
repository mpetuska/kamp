package dev.petuska.kamp.cli.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

suspend fun String.asDocument(): Document =
  withContext(Dispatchers.IO) { Jsoup.parse(this@asDocument) }
