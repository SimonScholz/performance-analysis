package io.github.simonscholz.scenario

import io.gatling.javaapi.core.CoreDsl.exec
import io.gatling.javaapi.core.CoreDsl.scenario
import io.gatling.javaapi.http.HttpDsl.http

object HelloScenario {
    private val hitHello =
        exec(
            http("Hit Hello").get("/hello")
        ).pause(1) // Gatling's default is seconds

    val hello = scenario("Hello").exec(hitHello)
}
