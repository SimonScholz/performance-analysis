package io.github.simonscholz.scenario

import io.gatling.javaapi.core.CoreDsl.* // ktlint-disable no-wildcard-imports
import io.gatling.javaapi.http.HttpDsl.http

object ChainScenario {
    private val chain =
        exec(
            http("Hit Hello").get("/hello"),
        ).pause(1) // Gatling's default is seconds
            .repeat(4, "i").on(
                exec(
                    http("Get Product Ids")
                        .get("/feederproducts")
                        .check(
                            jmesPath("[#{i}]")
                                .saveAs("productId"),
                        ),
                )
                    .pause(2)
                    .exec(
                        http("Hit Availability").get("/availability?productId=#{productId}"),
                    ),
            )

    val chainScenario = scenario("Hello").exec(chain)
}
