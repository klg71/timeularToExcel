package net.mayope.timeularToExcel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import java.io.File
import java.util.Scanner

val configFile = File(System.getProperty("user.home")+"/timeularClient/config")

data class ApiCredentials(val apiKey: String, val apiSecret: String)

fun main() {
    val objectMapper = ObjectMapper().apply { registerKotlinModule() }
    val credentials = loadCredentials(objectMapper)
    var token: String? = null
    val client = Feign.builder()
        .encoder(JacksonEncoder())
        .decoder(JacksonDecoder(objectMapper))
        .requestInterceptor {
            token?.let { token ->
                it.header("Authorization", "Bearer $token")
            }

        }
        .target(TimeularClient::class.java, "https://api.timeular.com/api/v3")

    token = client.token(TokenRequest(credentials.apiKey, credentials.apiSecret)).token
    println(token)

    client.activities().activities.forEach {
        println(it)
    }
    client.report().body().asReader(Charsets.UTF_8).readText().let {
        File("export.csv").writeText(it)
    }

}

private fun loadCredentials(objectMapper: ObjectMapper) = if (configFile.exists()) {
    println("Loading apiKey and apiSecret from: ${configFile.absolutePath}")
    objectMapper.readValue(configFile)
} else {
    val scanner = Scanner(System.`in`)
    println("Please enter the apiKey:")
    val apiKey = scanner.nextLine()
    println("Please enter the apiSecret:")
    val apiSecret = scanner.nextLine()
    println("Writing credentials into: ${configFile.absolutePath}")
    configFile.parentFile.mkdirs()
    ApiCredentials(apiKey, apiSecret).also {
        objectMapper.writeValue(configFile, it)
    }
}
