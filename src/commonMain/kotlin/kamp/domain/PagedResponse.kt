package kamp.domain

import kotlinx.serialization.*

public data class PagedResponse<T>(val data: List<T>, val nextPage: Int?, val size: Int, val total: Int)

@Serializable
public data class PagedHttpResponse<T>(val data: List<T>, val next: String?, val total: Int)
