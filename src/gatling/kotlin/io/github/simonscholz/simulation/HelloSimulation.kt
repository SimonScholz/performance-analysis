package io.github.simonscholz.simulation

/* ktlint-disable no-wildcard-imports */
import io.gatling.javaapi.core.CoreDsl.rampUsers
import io.gatling.javaapi.core.Simulation
import io.github.simonscholz.config.Config.HTTP_PROTOCOL
import io.github.simonscholz.scenario.CSVFeederProductAvailabilityScenario.productAvailabilityScenario
import io.github.simonscholz.scenario.CustomRestClientFeederProductAvailabilityScenario.restClientProductAvailabilityScenario
import io.github.simonscholz.scenario.HelloScenario.hello
import java.time.Duration

/* ktlint-disable no-wildcard-imports */

class HelloSimulation : Simulation() {

    init {
        val users = 10
        val duration = Duration.ofSeconds(30)
        setUp(
            hello.injectOpen(rampUsers(users).during(duration)),
            productAvailabilityScenario.injectOpen(rampUsers(users).during(duration)),
            restClientProductAvailabilityScenario.injectOpen(rampUsers(users).during(duration)),
        ).protocols(HTTP_PROTOCOL)
    }
}
