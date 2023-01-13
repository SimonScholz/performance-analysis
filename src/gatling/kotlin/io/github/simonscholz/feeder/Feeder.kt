package io.github.simonscholz.feeder

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.gatling.javaapi.core.CoreDsl.csv
import io.gatling.javaapi.core.CoreDsl.listFeeder
import io.github.simonscholz.config.Config
import java.net.URL

object Feeder {
    private val httpFeed: List<String> = URL("${Config.BASE_URL}/feederproducts").openStream().bufferedReader().use {
        ObjectMapper().readValue(it.readText())
    }

    val httpProductFeeder = listFeeder(httpFeed.map { mapOf("productId" to it) }).circular()

    val csvProductFeeder = csv("productIds.csv").circular()
}
