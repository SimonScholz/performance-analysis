package io.github.simonscholz.simulation

import io.gatling.javaapi.core.CoreDsl.* // ktlint-disable no-wildcard-imports
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http

class ComputerDatabaseSimulation : Simulation() {
    private val browse =
        repeat(4, "i").on(
            exec(
                http("Page #{i}").get("/computers?p=#{i}"),
            ).pause(1),
        )

    private val httpProtocol =
        http.baseUrl("https://computer-database.gatling.io")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0",
            )

    private val users = scenario("Users").exec(browse)

    init {
        setUp(
            users.injectOpen(rampUsers(10).during(10)),
        ).protocols(httpProtocol)
    }
}
