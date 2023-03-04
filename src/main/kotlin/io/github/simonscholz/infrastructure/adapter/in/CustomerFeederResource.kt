package io.github.simonscholz.infrastructure.adapter.`in`

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/feederproducts")
class CustomerFeederResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun productAvailability(): Response =
        Response
            .ok(listOf("1234", "5678", "91011", "121314", "151617", "181920"))
            .build()
}
