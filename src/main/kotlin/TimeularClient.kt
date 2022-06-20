package net.mayope.timeularToExcel

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import feign.RequestLine
import feign.Response
import java.util.UUID

data class TokenRequest(val apiKey: String, val apiSecret: String)
data class TokenAnswer(val token: String)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ActivityAnswer(val activities: List<Activity>)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Activity(val id: String,
    val name: String,
    val color: String,
    val integration: String,
    val spaceId: String,
    val deviceSide: String?)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TagAnswer(val tags: List<Tag>, val mentions: List<Tag>)

data class Tag(val id: String, val key: UUID, val label: String, val scope: String, val spaceId: String)

internal interface TimeularClient {
    @RequestLine("POST /developer/sign-in")
    fun token(request: TokenRequest): TokenAnswer

    @RequestLine("GET /activities")
    fun activities(): ActivityAnswer

    @RequestLine("GET /tags-and-mentions")
    fun tagsAndMentions(): TagAnswer

    @RequestLine("GET /report/2016-01-01T00:00:00.000/2022-12-31T23:59:59.999?timezone=Europe/Warsaw")
    fun report(): Response
}
