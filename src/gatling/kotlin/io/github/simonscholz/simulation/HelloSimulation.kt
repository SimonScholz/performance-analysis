package io.github.simonscholz.simulation

/* ktlint-disable no-wildcard-imports */
import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.core.Simulation
import io.gatling.javaapi.http.HttpDsl.http
import io.github.simonscholz.scenario.HelloScenario.hello
/* ktlint-disable no-wildcard-imports */

class HelloSimulation : Simulation() {

    private val httpProtocol =
        http.baseUrl("http://localhost:8080")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
            )

    init {
        setUp(
            hello.injectOpen(CoreDsl.rampUsers(50000).during(30)),
        ).protocols(httpProtocol)
    }
}
