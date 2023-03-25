package io.github.simonscholz.config

import io.gatling.javaapi.http.HttpDsl.http

object Config {
    val BASE_URL: String = System.getenv("gatlingBaseUrl") ?: "http://localhost:8080"

    val HTTP_PROTOCOL =
        http.baseUrl(BASE_URL)
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader(
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0",
            )
}
