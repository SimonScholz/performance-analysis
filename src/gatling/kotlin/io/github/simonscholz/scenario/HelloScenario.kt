package io.github.simonscholz.scenario

import io.gatling.javaapi.core.CoreDsl
import io.gatling.javaapi.http.HttpDsl

object HelloScenario {
    private val browse =
        // Note how we force the counter name, so we can reuse it
        CoreDsl.repeat(4, "i").on(
            CoreDsl.exec(
                HttpDsl.http("Page #{i}").get("/hello")
            ).pause(1)
        )

    val users = CoreDsl.scenario("Users").exec(browse)

}
