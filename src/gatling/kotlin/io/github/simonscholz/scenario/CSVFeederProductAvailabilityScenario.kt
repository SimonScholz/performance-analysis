package io.github.simonscholz.scenario

import io.gatling.javaapi.core.CoreDsl.feed
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.http.HttpDsl.http
import io.github.simonscholz.feeder.Feeder.csvProductFeeder

object CSVFeederProductAvailabilityScenario {
    private val hitAvailability =
        feed(csvProductFeeder)
            .exec(
                http("Hit Availability").get("/availability?productId=#{productId}"),
            ).pause(1) // Gatling's default is seconds

    val productAvailabilityScenario = scenario("ProductAvailabilityScenario").exec(hitAvailability)
}
