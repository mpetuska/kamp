#!/usr/bin/env kotlin

@file:DependsOn("io.projectreactor:reactor-core:3.4.11")
@file:DependsOn("org.jetbrains.kotlinx:kotlinx-cli-jvm:0.3.3")
@file:DependsOn("org.litote.kmongo:kmongo-coroutine-serialization:4.3.0")

import com.mongodb.client.model.*
import kotlinx.cli.*
import kotlinx.coroutines.runBlocking
import org.bson.Document
import org.litote.kmongo.*
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.reactivestreams.*

runBlocking {
  val parser = ArgParser("importData")
  val sourceUrl by parser.argument(ArgType.String, description = "Source mongo url")
  val targetUrl by parser.argument(
    ArgType.String,
    description = "Target mongo url"
  ) // .default("mongodb://localhost:27017")
  parser.parse(args)

  val sourceDB = KMongo.createClient(sourceUrl).coroutine
  val targetDB = KMongo.createClient(targetUrl).coroutine
  val targetCol = targetDB.getDatabase("kodex").getCollection<Document>("libraries")
  sourceDB.getDatabase("kodex").getCollection<Document>("libraries").find().consumeEach {
    val id = it["_id"]?.also {
      targetCol.deleteOneById(it)
    }
    println("Inserting $id")
    targetCol.insertOne(it)
  }
  sourceDB.close()
  targetDB.close()
}
