package dev.petuska.kamp.client.util

import io.ktor.client.HttpClient
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.accept
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.ContentTypeMatcher
import io.ktor.http.HttpHeaders
import io.ktor.http.content.ByteArrayContent
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.core.Input
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.readRemaining
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer

class CborFeature internal constructor(
  val serializer: CborKotlinxSerializer,
  val acceptContentTypes: List<ContentType>,
  private val receiveContentTypeMatchers: List<ContentTypeMatcher>,
) {
  internal constructor(config: Config) : this(
    config.serializer,
    config.acceptContentTypes,
    config.receiveContentTypeMatchers
  )

  class CborContentTypeMatcher : ContentTypeMatcher {
    override fun contains(contentType: ContentType): Boolean {
      if (ContentType.Application.Cbor.match(contentType)) {
        return true
      }

      val value = contentType.withoutParameters().toString()
      return value.startsWith("application/") && value.endsWith("+cbor")
    }
  }

  class CborKotlinxSerializer(
    private val cbor: Cbor = Cbor {}
  ) {

    fun write(data: Any, contentType: ContentType): OutgoingContent {
      @Suppress("UNCHECKED_CAST")
      return ByteArrayContent(writeContent(data), contentType)
    }

    private fun writeContent(data: Any): ByteArray =
      cbor.encodeToByteArray(buildSerializer(data, cbor.serializersModule), data)

    @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
    fun read(type: TypeInfo, body: Input): Any {
      val text = body.readBytes()
      val deserializationStrategy = cbor.serializersModule.getContextual(type.type)
      val mapper = deserializationStrategy ?: (type.kotlinType?.let { serializer(it) } ?: type.type.serializer())
      return cbor.decodeFromByteArray(mapper, text)!!
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildSerializer(value: Any, module: SerializersModule): KSerializer<Any> = when (value) {
      is JsonElement -> JsonElement.serializer()
      is List<*> -> ListSerializer(value.elementSerializer(module))
      is Array<*> -> value.firstOrNull()?.let { buildSerializer(it, module) } ?: ListSerializer(String.serializer())
      is Set<*> -> SetSerializer(value.elementSerializer(module))
      is Map<*, *> -> {
        val keySerializer = value.keys.elementSerializer(module)
        val valueSerializer = value.values.elementSerializer(module)
        MapSerializer(keySerializer, valueSerializer)
      }
      else -> {
        @OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
        module.getContextual(value::class) ?: value::class.serializer()
      }
    } as KSerializer<Any>

    @OptIn(ExperimentalSerializationApi::class)
    private fun Collection<*>.elementSerializer(module: SerializersModule): KSerializer<*> {
      val serializers: List<KSerializer<*>> =
        filterNotNull().map { buildSerializer(it, module) }.distinctBy { it.descriptor.serialName }

      if (serializers.size > 1) {
        error(
          "Serializing collections of different element types is not yet supported. " +
            "Selected serializers: ${serializers.map { it.descriptor.serialName }}"
        )
      }

      val selected = serializers.singleOrNull() ?: String.serializer()

      return if (selected.descriptor.isNullable) {
        selected
      } else {
        @Suppress("UNCHECKED_CAST")
        selected as KSerializer<Any>

        if (any { it == null }) selected.nullable else selected
      }
    }
  }

  /**
   * [CborFeature] configuration that is used during installation
   */
  class Config {
    /**
     * Serializer that will be used for serializing requests and deserializing response bodies.
     */
    var serializer: CborKotlinxSerializer = CborKotlinxSerializer()

    /**
     * Backing field with mutable list of content types that are handled by this feature.
     */
    private val _acceptContentTypes: MutableList<ContentType> = mutableListOf(ContentType.Application.Cbor)
    private val _receiveContentTypeMatchers: MutableList<ContentTypeMatcher> =
      mutableListOf(CborContentTypeMatcher())

    /**
     * List of content types that are handled by this feature.
     * It also affects `Accept` request header value.
     * Please note that wildcard content types are supported but no quality specification provided.
     */
    var acceptContentTypes: List<ContentType>
      set(value) {
        require(value.isNotEmpty()) { "At least one content type should be provided to acceptContentTypes" }

        _acceptContentTypes.clear()
        _acceptContentTypes.addAll(value)
      }
      get() = _acceptContentTypes

    /**
     * List of content type matchers that are handled by this feature.
     * Please note that wildcard content types are supported but no quality specification provided.
     */
    var receiveContentTypeMatchers: List<ContentTypeMatcher>
      set(value) {
        require(value.isNotEmpty()) { "At least one content type should be provided to acceptContentTypes" }
        _receiveContentTypeMatchers.clear()
        _receiveContentTypeMatchers.addAll(value)
      }
      get() = _receiveContentTypeMatchers

    /**
     * Adds accepted content types. Be aware that [ContentType.Application.Cbor] accepted by default is removed from
     * the list if you use this function to provide accepted content types.
     * It also affects `Accept` request header value.
     */
    fun accept(vararg contentTypes: ContentType) {
      _acceptContentTypes += contentTypes
    }

    /**
     * Adds accepted content types. Existing content types will not be removed.
     */
    fun receive(matcher: ContentTypeMatcher) {
      _receiveContentTypeMatchers += matcher
    }
  }

  internal fun canHandle(contentType: ContentType): Boolean {
    val accepted = acceptContentTypes.any { contentType.match(it) }
    val matchers = receiveContentTypeMatchers

    return accepted || matchers.any { matcher -> matcher.contains(contentType) }
  }

  /**
   * Companion object for feature installation
   */
  companion object Feature : HttpClientFeature<Config, CborFeature> {
    override val key: AttributeKey<CborFeature> = AttributeKey("Cbor")

    override fun prepare(block: Config.() -> Unit): CborFeature {
      val config = Config().apply(block)
      val serializer = config.serializer
      val allowedContentTypes = config.acceptContentTypes.toList()
      val receiveContentTypeMatchers = config.receiveContentTypeMatchers

      return CborFeature(serializer, allowedContentTypes, receiveContentTypeMatchers)
    }

    override fun install(feature: CborFeature, scope: HttpClient) {
      scope.requestPipeline.intercept(HttpRequestPipeline.Transform) { payload ->
        feature.acceptContentTypes.forEach { context.accept(it) }

        val contentType = context.contentType() ?: return@intercept
        if (!feature.canHandle(contentType)) return@intercept

        context.headers.remove(HttpHeaders.ContentType)

        val serializedContent = when (payload) {
          Unit -> EmptyContent
          is EmptyContent -> EmptyContent
          else -> feature.serializer.write(payload, contentType)
        }

        proceedWith(serializedContent)
      }

      scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
        if (body !is ByteReadChannel) return@intercept

        val contentType = context.response.contentType() ?: return@intercept
        if (!feature.canHandle(contentType)) return@intercept

        val parsedBody = feature.serializer.read(info, body.readRemaining())
        val response = HttpResponseContainer(info, parsedBody)
        proceedWith(response)
      }
    }
  }
}
