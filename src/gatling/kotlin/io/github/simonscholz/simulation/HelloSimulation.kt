package io.github.simonscholz.simulation

/* ktlint-disable no-wildcard-imports */
import io.gatling.javaapi.core.CoreDsl.*
import io.gatling.javaapi.core.Simulation
import io.github.simonscholz.config.Config.HTTP_PROTOCOL
import io.github.simonscholz.scenario.CSVFeederProductAvailabilityScenario.productAvailabilityScenario
import io.github.simonscholz.scenario.ChainScenario.chainScenario
import io.github.simonscholz.scenario.CustomRestClientFeederProductAvailabilityScenario.restClientProductAvailabilityScenario
import io.github.simonscholz.scenario.HelloScenario.hello
import java.time.Duration

/* ktlint-disable no-wildcard-imports */

/**
 * Renamed to skip this simulation for now,
 * because the simulation closure with includes does not work atm.
 */
class HelloSimulation : Simulation() {

    init {
        val users = 10
        val duration = Duration.ofSeconds(30)
        setUp(
            hello.injectOpen(
                nothingFor(Duration.ofSeconds(2)),
                atOnceUsers(50),
                rampUsers(users).during(duration),
                rampUsersPerSec(20.0).to(50.0).during(20),
            ),
            productAvailabilityScenario.injectOpen(
                nothingFor(Duration.ofSeconds(2)),
                atOnceUsers(50),
                rampUsers(users).during(duration),
                rampUsersPerSec(20.0).to(50.0).during(20),
            ),
            restClientProductAvailabilityScenario.injectOpen(
                nothingFor(Duration.ofSeconds(2)),
                atOnceUsers(50),
                rampUsers(users).during(duration),
                rampUsersPerSec(20.0).to(50.0).during(20),
            ),
            chainScenario.injectOpen(
                nothingFor(Duration.ofSeconds(2)),
                atOnceUsers(50),
                rampUsers(users).during(duration),
                rampUsersPerSec(20.0).to(50.0).during(20),
            ),
        ).protocols(HTTP_PROTOCOL)
    }
}
