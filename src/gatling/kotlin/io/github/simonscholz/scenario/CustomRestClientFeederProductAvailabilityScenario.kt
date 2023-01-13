package io.github.simonscholz.scenario

import io.gatling.javaapi.core.CoreDsl.feed
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.http.HttpDsl
import io.github.simonscholz.feeder.Feeder.httpProductFeeder

object CustomRestClientFeederProductAvailabilityScenario {

    private val hitAvailability =
        feed(httpProductFeeder)
            .exec(
                HttpDsl.http("Hit Availability fed by rest call").get("/availability?productId=#{productId}"),
            ).pause(1) // Gatling's default is seconds

    val restClientProductAvailabilityScenario = scenario("RestClientProductAvailabilityScenario").exec(hitAvailability)
}
